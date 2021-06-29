package com.buuble.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String fileSavePath = System.getProperty("user.dir") +
            "\\src\\main\\resources\\static\\avatar/";

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + fileSavePath);
        registry.addResourceHandler("/blogImages/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "\\src\\main\\resources\\static\\blogImages/");
    }
}

