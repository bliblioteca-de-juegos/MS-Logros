package com.biblioteca.logros.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI logrosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API MS-Logros")
                .version("1.0")
                .description("Gestion de logros y progreso por usuario y juego."));
    }
}
