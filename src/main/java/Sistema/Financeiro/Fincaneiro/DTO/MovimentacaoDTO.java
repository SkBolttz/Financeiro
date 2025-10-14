package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.Periodicidade;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimentacaoDTO(
                Long id,
                @NotBlank(message = "A descrição é obrigatória") String descricao,
                @NotNull(message = "O valor é obrigatório") @Positive(message = "O valor deve ser maior que zero") Double valor,
                @NotNull(message = "O tipo é obrigatório") TipoMovimentacao tipo,
                @NotNull(message = "A data é obrigatória") LocalDate data,
                @NotNull(message = "O usuário é obrigatório") Usuario usuario_id,
                @NotNull(message = "A categoria é obrigatória") Categoria categoria_id,
                @NotNull(message = "O tipo de pagamento é obrigatório") TipoPagamento tipoPagamento,
                Boolean ativa,
                Boolean lancamentoRecorrente,
                Periodicidade periodicidade, // 🔹 novo
                LocalDate dataFimRecorrencia, // 🔹 novo
                Integer totalRecorrencias, // 🔹 novo
                String comprovanteEntrada,
                String comprovanteRestante,
                Cliente cliente,
                Fornecedor fornecedor) {
}
