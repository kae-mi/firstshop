package com.shopshop.firstshop.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.dir}")
    private String uploadPath;

    @PostConstruct
    public void logUploadPath(){
        System.out.println("Upload Path: " + uploadPath);
    }

    // 클라이언트가 /images/** 경로로 요청을 보내면 정적 리소스의 실제 파일 경로를 찾아서 클라이언트에게 반환한다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadPath);
    }

}
