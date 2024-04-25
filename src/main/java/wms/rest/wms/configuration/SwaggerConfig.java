package wms.rest.wms.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Access Application Programming Interface (API) documentation at http://localhost:8080/swagger-ui/index.html#/
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures Swagger for the public APIs.
     *
     * @return a grouped OpenAPI definition for public APIs.
     */
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * Configures custom OpenApi settings for hte WMS API.
     *
     * @return an OpenAPI definition with custom settings.
     */
    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("WMS API").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
