package com.bookstorage.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("🔧 Swagger configuration initialized - API documentation available at /api/swagger-ui.html");
        final String securitySchemeName = "bearerAuth";

        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Online Library API")
                        .description("A comprehensive API for managing an online library system with user authentication, book management, and admin features.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Online Library Team")
                                .email("support@onlinelibrary.com")
                                .url("https://onlinelibrary.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));

        logger.info("📚 Swagger API documentation configured with JWT authentication support");
        return openAPI;
    }
}
