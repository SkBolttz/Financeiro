package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import jakarta.validation.constraints.NotNull;

public record RemoverMovimentacaoDTO(
        @NotNull Long id,
        @NotNull Usuario usuario) {

}
