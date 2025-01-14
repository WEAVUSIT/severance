package com.weavus.weavusys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("weavusys 인사정보 Swagger 테스트 페이지")
                        .description("weavusys API 모음")
                        .version("1.0.0")
                        .contact(new Contact().name("weavusys").email("")));
    }
}