package com.study.demo.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;


import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload.root:./uploads}")
    private String uploadRoot;

    @Value("${file.upload.public-prefix:/uploads}")
    private String publicPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 예: /uploads/**  ->  file:{프로젝트절대경로}/uploads/
        String handler  = publicPrefix + "/**";
        String location = "file:" + Paths.get(uploadRoot).toAbsolutePath() + "/";

        registry.addResourceHandler(handler)
                .addResourceLocations(location);
    }
}