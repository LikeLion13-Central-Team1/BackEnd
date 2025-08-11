package com.study.demo.backend.global.ImgUploader;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String upload(MultipartFile file, String dir);
}
