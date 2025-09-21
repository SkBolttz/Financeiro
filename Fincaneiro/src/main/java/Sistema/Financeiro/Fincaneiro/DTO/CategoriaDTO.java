package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
         Long id,
        @NotBlank String nome,
        TipoMovimentacao tipo) {

}
