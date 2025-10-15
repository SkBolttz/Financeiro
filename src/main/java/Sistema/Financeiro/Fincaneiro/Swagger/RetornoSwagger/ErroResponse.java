package Sistema.Financeiro.Fincaneiro.Swagger.RetornoSwagger;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de resposta de erro padrão da API")
public record ErroResponse(
        @Schema(description = "Mensagem de erro detalhada", example = "Token inválido ou expirado") String mensagem,

        @Schema(description = "Código HTTP do erro", example = "401") int status) {
}
