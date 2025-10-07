package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlterarClienteDTO {

    @NotNull
    private Long id;

    private String nome;

    private String cpf;

    private String cnpj;

    private String telefone;

    private String email;

    private Endereco endereco;

    private TipoClienteFornecedor tipo;
}
