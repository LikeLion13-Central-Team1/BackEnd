package com.study.demo.backend.global.config;

import com.study.demo.backend.domain.auth.service.command.JwtTokenCommandService;
import com.study.demo.backend.domain.auth.service.query.JwtTokenQueryService;
import com.study.demo.backend.domain.user.repository.UserRepository;
import com.study.demo.backend.global.security.exception.JwtAccessDeniedHandler;
import com.study.demo.backend.global.security.exception.JwtAuthenticationEntryPoint;
import com.study.demo.backend.global.security.filter.CustomLoginFilter;
import com.study.demo.backend.global.security.filter.JwtAuthorizationFilter;
import com.study.demo.backend.global.security.handler.CustomLogoutHandler;
import com.study.demo.backend.global.security.utils.JwtUtil;
import com.study.demo.backend.global.utils.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenCommandService jwtTokenCommandService;
    private final JwtTokenQueryService jwtTokenQueryService;

    //인증이 필요하지 않은 url
    private final String[] allowedUrls = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/auth/login",
            "/api/v1/auth/login/customer",
            "/api/v1/auth/login/owner",
            "/uploads/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 정책 설정
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        // CSRF 토큰 설정
//        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers("/api/auth/login", "/api/auth/signup")
//                );
        http.
                csrf(AbstractHttpConfigurer::disable);

        // form 로그인 방식 비활성화 -> REST API 로그인을 사용할 것이기 때문에
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않음. (세션 생성 정책을 Stateless 설정.)
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                );

        // 경로별 인가
        http
                .authorizeHttpRequests(auth -> auth
                        //위에서 정의했던 allowedUrls 들은 인증이 필요하지 않음 -> permitAll
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated() // 그 외의 url 들은 인증이 필요함
                );

        // CustomLoginFilter 인스턴스를 생성하고 필요한 의존성을 주입
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, userRepository);
        // Login Filter URL 지정
        customLoginFilter.setFilterProcessesUrl("/api/v1/auth/login");
        // 필터 체인에 CustomLoginFilter를 UsernamePasswordAuthenticationFilter 자리에서 동작하도록 추가
        http
                .addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
        // JwtFilter를 CustomLoginFilter 뒤에서 동작하도록 필터 체인에 추가
        http
                .addFilterAfter(new JwtAuthorizationFilter(jwtUtil, userRepository), CustomLoginFilter.class);

        // Logout Handler 추가
        http
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(new CustomLogoutHandler(jwtTokenCommandService, jwtTokenQueryService, jwtUtil))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            try {
                                HttpResponseUtil.setSuccessResponse(
                                        response,
                                        HttpStatus.OK,
                                        "로그아웃이 완료되었습니다."
                                );
                            } catch (IOException e) {
                                // 예외 처리
                                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                            }
                        })
                );

        return http.build();
    }
}
