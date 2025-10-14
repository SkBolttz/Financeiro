package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoCategoriaDTO {
    private String categoria;
    private Double total;
    private TipoMovimentacao tipo;
}
