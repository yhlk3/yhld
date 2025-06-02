package com.example.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppLoginValidationInterceptor())
                .addPathPatterns("/user/login")
                .addPathPatterns("/user/register");
        registry.addInterceptor(new AppLoggedInValidationInterceptor())
                .addPathPatterns("/category/*");
    }
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 允许所以的源
                .addMapping("/**")
                .allowedOrigins("http://localhost:8080")

                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许发送Cookie
    }
}
     