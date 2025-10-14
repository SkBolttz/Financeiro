package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Enum.Periodicidade;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public record AlterarMovimentacaoDTO(
        @NotNull Long id,
        Categoria categoria_id,
        TipoMovimentacao tipo,
        Double valor,
        LocalDate data,
        String descricao,
        TipoPagamento tipoPagamento,
        Boolean lancamentoRecorrente,
        Periodicidade periodicidade,
        LocalDate dataFimRecorrencia,
        Integer totalRecorrencias,
        String comprovanteEntrada,
        String comprovanteRestante,
        Cliente cliente,
        Fornecedor fornecedor,
        boolean pago) {
}
