package com.study.demo.backend.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${webclient.connection-timeout}")
    private int connectionTimeout;

    @Value("${webclient.read-timeout}")
    private int readTimeout;

    @Value("${webclient.write-timeout}")
    private int writeTimeout;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    @Bean(name = "openaiWebClient")
    public WebClient openaiWebClient() {
        return createWebClient(openaiApiUrl)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .filter(generalErrorHandler())
                .build();
    }

    /**
     * 공통 WebClient 생성 (기본 HTTP 설정 포함)
     */
    public WebClient.Builder createWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)))
                .responseTimeout(Duration.ofMillis(readTimeout));

        WebClient.Builder builder = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .filter(logResponse());

        if (baseUrl != null && !baseUrl.isEmpty()) {
            builder.baseUrl(baseUrl);
        }

        return builder;
    }

    /**
     * 공통 요청 로깅 필터
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            HttpHeaders masked = new HttpHeaders();
            req.headers().forEach((k, v) -> {
                if ("Authorization".equalsIgnoreCase(k)) {
                    masked.add(k, "Bearer ****(masked)****");
                } else {
                    masked.addAll(k, v);
                }
            });
            log.debug("External API Request: {} {}", req.method(), req.url());
            log.debug("Request Headers: {}", masked);
            return Mono.just(req);
        });
    }

    /**
     * 공통 응답 로깅 필터
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.debug("External API Response Status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    /**
     * 일반적인 에러 핸들러
     */
    public ExchangeFilterFunction generalErrorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("External API Error Response: {}", errorBody);
                            return Mono.error(new RuntimeException("외부 API 호출 실패: " + clientResponse.statusCode()));
                        });
            }
            return Mono.just(clientResponse);
        });
    }
}
