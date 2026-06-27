package com.caa.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

 // Swagger/OpenAPI Configuration. Provides interactive API documentation at /swagger-ui.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CAA Board API")
                        .version("1.0.0")
                        .description("REST API for Augmentative and Alternative Communication (AAC) Board Management System" +
                                "Los pictogramas utilizados son propiedad del Gobierno de Aragón, creados por Sergio Palao " +
                                "para ARASAAC (arasaac.org), bajo licencia Creative Commons BY-NC-SA 4.0."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server")
                ));
    }
}