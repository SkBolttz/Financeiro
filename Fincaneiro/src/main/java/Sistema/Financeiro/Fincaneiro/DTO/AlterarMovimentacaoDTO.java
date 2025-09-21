package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;

public record AlterarMovimentacaoDTO(
        @NotNull Long id,
        Categoria categoria_id,
        TipoMovimentacao tipo,
        Double valor,
        LocalDate data,
        String descricao) {

}
