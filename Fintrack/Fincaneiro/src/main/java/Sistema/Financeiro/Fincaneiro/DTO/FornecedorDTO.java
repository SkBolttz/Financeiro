package Sistema.Financeiro.Fincaneiro.DTO;

import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Endereco;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FornecedorDTO {

    @NotBlank
    private String razaoSocial;
    
    private String cnpj;
    private String cpf;
    
    @NotBlank
    private String telefone;
    
    @NotBlank
    @Email
    private String email;
    
    @NotNull
    private Endereco endereco;
    
    @NotBlank
    private String formaPagamento;
    
    private Long banco;
    private Long agencia;
    private Long conta;
    
    @NotBlank
    private String pessoaContato;
    
    private String observacao;
    
    @NotNull
    private TipoClienteFornecedor tipo;

    @NotNull
    private Usuario usuario;
}
