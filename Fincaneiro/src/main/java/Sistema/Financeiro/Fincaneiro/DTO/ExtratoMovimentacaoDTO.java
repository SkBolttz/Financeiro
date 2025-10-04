package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

public record ExtratoMovimentacaoDTO(
        String descricao,
        String categoria,
        Double valor,
        LocalDate data) {

}
