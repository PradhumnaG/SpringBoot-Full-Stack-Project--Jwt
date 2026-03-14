 package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

 @Configuration
 public class SwaggerConfigNew {

     public OpenAPI customOpenAPI() {
         return new OpenAPI()
                 .info(new Info()
                         .title("Hello Doctor API")
                         .version("1.0")
                         .description("API documentation for the Hello Doctor application"));
     }
 }
