package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoPagamento;

public record MovimentacaoTipoPagamentoDTO(
    TipoPagamento tipoPagamento,
    Double total,
    TipoMovimentacao tipo
) {}
