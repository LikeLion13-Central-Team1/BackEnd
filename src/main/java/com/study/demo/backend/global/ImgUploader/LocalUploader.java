package com.study.demo.backend.global.ImgUploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class LocalUploader implements FileUploader {

    @Value("${file.upload.root:./uploads}")
    private String uploadRoot;

    @Value("${file.upload.public-prefix:/uploads}")
    private String publicPrefix;

    @Value("${file.upload.domain}")
    private String domain;

    @Override
    public String upload(MultipartFile file, String dir) {
        if (file == null || file.isEmpty()) return null;

        try {
            Path saveDir = Paths.get(uploadRoot, dir).toAbsolutePath().normalize();
            Files.createDirectories(saveDir);

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null) {
                int dot = original.lastIndexOf('.');
                if (dot >= 0 && dot < original.length() - 1) {
                    ext = original.substring(dot);
                }
            }
            String savedName = UUID.randomUUID() + ext;

            Path target = saveDir.resolve(savedName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            String base = domain.endsWith("/") ? domain.substring(0, domain.length() - 1) : domain;
            String prefix = publicPrefix.startsWith("/") ? publicPrefix : "/" + publicPrefix;
            return base + prefix + "/" + dir + "/" + savedName;

        } catch (Exception e) {
            log.error("Local upload failed: root={}, dir={}, size={}, contentType={}, reason={}",
                    uploadRoot, dir, file.getSize(), file.getContentType(), e.getMessage(), e);
            throw new RuntimeException("로컬 이미지 업로드 실패", e);
        }
    }
}