package com.study.demo.backend.domain.menu.service.command;

import com.study.demo.backend.domain.menu.dto.request.MenuReqDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GPTImgService {
    String generateDescription(MenuReqDTO.GenerateDescription request, MultipartFile menuImage);
}