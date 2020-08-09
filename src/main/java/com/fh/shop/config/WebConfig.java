package com.fh.shop.config;

import com.fh.shop.interceptor.IdempotentInterceptor;
import com.fh.shop.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class WebConfig implements WebMvcConfigurer {


    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(idempotentInterceptor()).addPathPatterns("/api/**");
    }





    @Bean
     public LoginInterceptor loginInterceptor(){
         return  new LoginInterceptor();
    }
    @Bean
    public IdempotentInterceptor idempotentInterceptor(){
        return  new IdempotentInterceptor();
    }
}
