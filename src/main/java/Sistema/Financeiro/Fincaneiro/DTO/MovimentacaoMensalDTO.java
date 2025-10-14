package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

public record MovimentacaoMensalDTO(
    Integer mes,
    Double total,
    TipoMovimentacao tipo
) {}
