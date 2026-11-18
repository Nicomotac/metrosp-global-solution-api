package br.com.fiap.metrosp.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.*;
@Configuration
public class OpenApiConfig {
 @Bean OpenAPI openApi(){return new OpenAPI().info(new Info().title("MetroSP - FIAP Global Solution").version("1.0.0")
 .description("API de estações, ocorrências e autenticação do Metrô de São Paulo"))
 .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
 .components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));}
}
