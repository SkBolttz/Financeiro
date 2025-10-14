package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarFornecedorDTO {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String cpf;
    private String telefone;
    @Email
    private String email;
    private Endereco endereco;
    private String formaPagamento;
    private Long banco;
    private Long agencia;
    private Long conta;
    private String pessoaContato;
    private String observacao;
    private TipoClienteFornecedor tipo;
    private Usuario usuario;
}
