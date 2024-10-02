package com.kooz.giggy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("/");

        Info info = new Info()
                .title("Swagger API")
                .version("1.0.0")
                .description("Swagger API");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
