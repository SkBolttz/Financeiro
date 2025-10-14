package Sistema.Financeiro.Fincaneiro.Swagger.ConfigSwagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinTrack")
                        .version("v1.0")
                        .description("Documentação da API do sistema de controle financeiro FinTrack.")
                        .contact(new Contact()
                                .name("Henrique B.")
                                .email("henrique@email.com")));
    }
}
