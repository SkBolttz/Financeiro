package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record MovimentacaoDTO(
        Long id,
        @NotBlank(message = "A descrição é obrigatória") String descricao,

        @NotNull(message = "O valor é obrigatório") @Positive(message = "O valor deve ser maior que zero") Double valor,

        @NotNull(message = "O tipo é obrigatório") TipoMovimentacao tipo,

        @NotNull(message = "A data é obrigatória") @PastOrPresent(message = "A data não pode ser futura") LocalDate data,

        @NotNull(message = "O usuário é obrigatório") Usuario usuario_id,

        @NotNull(message = "A categoria é obrigatória") Categoria categoria_nome,

        Boolean ativa) {
}
