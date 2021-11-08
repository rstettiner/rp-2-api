package com.rafa.web.config;

import com.rafa.web.filters.ServiceContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.rafa")
public class WebMvcConfiguration implements WebMvcConfigurer {
    
    private final ServiceContextInterceptor serviceContextInterceptor;

    @Autowired
    public WebMvcConfiguration(ServiceContextInterceptor serviceContextInterceptor
    ) {
        this.serviceContextInterceptor = serviceContextInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(serviceContextInterceptor);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200", "http://localhost:8080")
            .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS");
    }
}
