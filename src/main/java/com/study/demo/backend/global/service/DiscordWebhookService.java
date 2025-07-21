package com.study.demo.backend.global.service;

import com.study.demo.backend.global.config.DiscordWebhookConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscordWebhookService {

    private final RestTemplate restTemplate;
    private final DiscordWebhookConfig discordWebhookConfig;

    @Profile("!local") // local 프로필이 아닐 때만 실행
    public void sendErrorToDiscord(Exception exception, HttpServletRequest request) {
        try {
            String url = discordWebhookConfig.getDiscordWebhookUrl();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> data = new HashMap<>();

            // 임베드 포맷 (Discord용)
            Map<String, Object> embed = new HashMap<>();
            embed.put("title", "🚨 500 서버 에러 발생!");
            embed.put("color", 16711680); // 빨간색

            String description = String.format(
                    "**시간**: %s\n" +
                            "**요청 URL**: %s %s\n" +
                            "**에러 타입**: %s\n" +
                            "**에러 메시지**: %s",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    request.getMethod(), request.getRequestURI(),
                    exception.getClass().getName(),
                    exception.getMessage() != null ? exception.getMessage() : "메시지 없음"
            );

            embed.put("description", description);

            data.put("embeds", new Object[]{embed});

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
            restTemplate.postForEntity(url, requestEntity, String.class);
        } catch (Exception e) {
            // 웹훅 전송 실패 시 콘솔에 로그
            System.err.println("Discord 웹훅 전송 실패: " + e.getMessage());
        }
    }
}
