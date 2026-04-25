package com.divami.java_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes Swagger UI at /swagger-ui.html and OpenAPI JSON at /v3/api-docs.
 * All protected endpoints require a Bearer JWT token — add it via the Authorize button in Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Family League API",
                version = "1.0",
                description = "Backend API for the Family League cricket prediction platform."
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    /**
     * Replaces springdoc's default array-type sort parameter with a plain string field.
     * This prevents Swagger UI from sending sort=[] or sort=["string"] when the field is empty.
     * Usage in Swagger: type "username,asc" or "createdAt,desc", or leave blank for default sort.
     */
    @Bean
    public OperationCustomizer sortParameterCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() == null) return operation;

            boolean hasPagination = operation.getParameters().stream()
                    .anyMatch(p -> "page".equals(p.getName()));
            if (!hasPagination) return operation;

            operation.getParameters().removeIf(p -> "sort".equals(p.getName()));

            operation.addParametersItem(new Parameter()
                    .name("sort")
                    .in("query")
                    .required(false)
                    .description("Sort field and direction. Example: username,asc — leave blank for default.")
                    .schema(new StringSchema()));

            return operation;
        };
    }
}
