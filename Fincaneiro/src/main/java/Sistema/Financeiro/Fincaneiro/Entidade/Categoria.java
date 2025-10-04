package Sistema.Financeiro.Fincaneiro.Entidade;

import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    private boolean ativa;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    public Categoria(String nome, TipoMovimentacao tipo, boolean ativa) {
        this.nome = nome;
        this.tipo = tipo;
        this.ativa = ativa;
    }
}
