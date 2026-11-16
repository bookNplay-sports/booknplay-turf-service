package com.booknplay.turf.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI turfServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BooknPlay Turf Service API")
                        .description("API documentation for Turf Management")
                        .version("1.0.0"));
    }
}
