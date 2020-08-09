package com.fh.shop.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {





    @Bean
    public Docket adminApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fh.shop"))
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.any()))
                .build();

    }



    private ApiInfo adminApiInfo(){

        return new ApiInfoBuilder()
                .title("飞狐乐购")
                .description("飞狐电商接口管理")
                .version("1.0")
                .contact(new Contact("李毅杰", "http://atguigu.com", "1203633594@qq.com"))
                .build();
    }
}


