package com.study.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("潘多码SpringBoot3+Vue3脚手架")
                        .version("1.0")
                        .description( "潘多码SpringBoot3+Vue3脚手架接口文档")
                        .contact(new Contact().name("潘多码").url("www.panduoma.com")));
    }

}
