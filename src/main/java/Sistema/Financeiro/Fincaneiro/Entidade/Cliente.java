package Sistema.Financeiro.Fincaneiro.Entidade;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "tb_cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    private String cpf;
    private String cnpj;

    @NotBlank
    private String telefone;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @ManyToOne
    private Endereco endereco;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoClienteFornecedor tipo;

    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate dataCadastro;
}
