package com.study.demo.backend.domain.menu.service.command;

import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import com.study.demo.backend.domain.menu.exception.MenuErrorCode;
import com.study.demo.backend.domain.review.dto.response.ReviewResDTO;
import com.study.demo.backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class GPTImgServiceImpl implements GPTImgService {

    @Qualifier("openaiWebClient")
    private final WebClient openaiWebClient;

    @Value("${openai.model}")
    private String model;

    @Override
    public String generateDescription(MenuReqDTO.GenerateDescription request, MultipartFile menuImage) {
        log.info("AI 설명 생성 요청 수신. 파일명: {}, 타입: {}, 크기: {} bytes",
                menuImage.getOriginalFilename(), menuImage.getContentType(),menuImage.getSize());

        if (menuImage == null || menuImage.isEmpty()) {
            throw new CustomException(MenuErrorCode.MENU_IMAGE_REQUIRED);
        }

        try {
            String base64Image = encodeImageToBase64(menuImage);
            String contentType = menuImage.getContentType();

            Map<String, Object> body = createBody(request.menuName(), base64Image, contentType);

            return openaiWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(ReviewResDTO.GPTResponse.class)
                    .map(res -> res.choices().get(0).message().content().trim())
                    .onErrorReturn("이미지 설명 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                    .block();

        } catch (IOException e) {
            log.error("이미지 파일을 Base64로 인코딩하는 데 실패했습니다.", e);
            throw new RuntimeException("이미지 처리 중 서버 내부 오류가 발생했습니다.", e);
        } catch (Exception e) {
            log.warn("OpenAI Vision API 호출 중 예외가 발생했습니다.", e);
            return "이미지 설명 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        }
    }

    private String encodeImageToBase64(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private Map<String, Object> createBody(String menuName, String base64Image, String contentType) {
        String promptText = """
            [역할]
            당신은 고객의 입맛을 돋우는 전문 메뉴 카피라이터입니다.
            
            [임무]
            아래 [메뉴 이름]과 [이미지]를 보고, 고객이 주문하고 싶게 만드는 매력적인 메뉴 설명을 생성해주세요.
            
            [작성 규칙]
            - 친근하면서도 전문성이 느껴지는 톤을 유지해주세요.
            - 메뉴의 핵심 재료, 맛, 식감을 중심으로 1~2문장의 간결한 한국어로 작성해주세요.
            - 고객의 오감을 자극하는 표현을 사용해주세요. (예: '바삭한', '육즙 가득한', '싱싱한', '건강한', '두툼한')
            - 사진에서 보이지 않는 재료를 상상해서 쓰지 마세요.
            - 메뉴 이름만 단순히 반복해서 설명하지 마세요.
            - 너무 전문적이거나 어려운 요리 용어는 피해주세요.
            
            [예외 처리]
            - 만약 제공된 이미지가 음식이 아니거나 메뉴를 설명하기에 부적절하다면, 다른 말 없이 오직 "메뉴 설명을 생성할 수 없는 이미지입니다." 라고만 답변해주세요.
            
            [메뉴 이름]
            %s
            """.formatted(menuName);

        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "text", "text", promptText),
                                Map.of(
                                        "type", "image_url",
                                        "image_url", Map.of("url", "data:%s;base64,%s".formatted(contentType, base64Image))
                                )
                        ))
                ),
                "max_tokens", 200 // 설명이 조금 길어질 수 있으니 넉넉하게 설정
        );
    }
}