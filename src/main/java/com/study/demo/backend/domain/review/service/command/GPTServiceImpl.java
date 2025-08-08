package com.study.demo.backend.domain.review.service.command;

import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTServiceImpl implements GPTService {

    @Qualifier("openaiWebClient")
    private final WebClient openaiWebClient;

    @Value("${openai.model}")
    private String model;

    @Override
    public String summarizeFromContents(List<String> rawContents) {
        List<String> contents = sanitizeAndClip(rawContents);
        if (contents.isEmpty()) return "요약할 리뷰가 없습니다.";

        String prompt = buildParagraphPrompt(contents);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "당신은 한국어로 간결하고 사실 기반으로 요약하는 어시스턴트입니다. " +
                                        "불필요한 수식/과장/추측을 하지 마세요."),
                        Map.of("role", "user", "content", prompt)
                ),
                // 문단형은 일관성이 중요해서 낮은 temperature 권장
                "temperature", 0.3
        );

        try {
            return openaiWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(ReviewResDTO.GPTResponse.class)
                    .map(res -> res.choices().get(0).message().content().trim())
                    .onErrorReturn("리뷰 요약 중 오류가 발생했습니다.")
                    .block();
        } catch (Exception e) {
            log.warn("OpenAI 호출 실패", e);
            return "리뷰 요약 중 오류가 발생했습니다.";
        }
    }

    /** null/공백 제거 + 개수/길이/총량 제한으로 토큰 보호 */
    private List<String> sanitizeAndClip(List<String> raw) {
        if (raw == null) return List.of();

        List<String> cleaned = raw.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        int N = Math.min(cleaned.size(), 50); // 개수 제한
        cleaned = cleaned.subList(0, N);

        int PER_REVIEW_MAX = 400; // 개별 길이 제한
        cleaned = cleaned.stream()
                .map(s -> s.length() > PER_REVIEW_MAX ? s.substring(0, PER_REVIEW_MAX) + "…" : s)
                .toList();

        int TOTAL_MAX = 6000; // 전체 길이 제한
        StringBuilder sum = new StringBuilder();
        List<String> clipped = new ArrayList<>();
        for (String c : cleaned) {
            if (sum.length() + c.length() + 2 > TOTAL_MAX) break;
            clipped.add(c);
            sum.append(c).append('\n');
        }
        return clipped;
    }

    /** 문단형 요약 프롬프트 */
    private String buildParagraphPrompt(List<String> contents) {
        String joined = contents.stream()
                .map(c -> "- " + c.replace("\n", " "))
                .collect(Collectors.joining("\n"));

        return """
               아래는 한 가게에 대한 실제 사용자 리뷰 목록입니다.
               리뷰 전반에서 반복적으로 언급되는 특징을 중심으로, 자연스럽게 이어지는 한 단락으로 요약해 주세요.
               - 번호 목록/불릿/총평/머리말 없이 하나의 문단으로만 작성
               - 과장/추측 금지, 사실 기반
               - 3~5문장 분량 권장
               
               [리뷰 목록]
               %s
               """.formatted(joined);
    }
}
