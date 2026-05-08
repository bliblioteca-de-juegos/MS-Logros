package com.biblioteca.logros.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient usuarioWebClient(
            WebClient.Builder builder,
            @Value("${ms.usuarios.url}") String usuariosUrl) {
        return builder.baseUrl(usuariosUrl).build();
    }
}
