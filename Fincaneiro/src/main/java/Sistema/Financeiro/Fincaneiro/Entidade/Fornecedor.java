package Sistema.Financeiro.Fincaneiro.Entidade;

import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    @ManyToOne
    private Endereco endereco;
    @NotBlank
    private String formaPagamento;
    private Long banco;
    private Long agencia;
    private Long conta;
    private long codigoInterno;
    @NotBlank
    private String pessoaContato;
    private String observacao;
    @NotNull
    private TipoClienteFornecedor tipo;
    private boolean ativo = true;
}
