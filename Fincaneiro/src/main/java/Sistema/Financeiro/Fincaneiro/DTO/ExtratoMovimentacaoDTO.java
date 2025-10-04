package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

public record ExtratoMovimentacaoDTO(
        String descricao,
        String categoria,
        Double valor,
        LocalDate data,
        TipoMovimentacao tipo) {
}
