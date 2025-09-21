package Sistema.Financeiro.Fincaneiro.Entidade;

import java.time.LocalDate;
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
@Table(name = "tb_movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario_id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
    private Categoria categoria_id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    @NotNull
    private Double valor;
    private LocalDate data;
    @NotBlank
    private String descricao;
    private Boolean pago;
    private boolean atrasado;
    private boolean ativa;
    private LocalDate dataDeDesativacao;
    private LocalDate dataDePagamento;

    public Movimentacao(String descricao, Double valor, LocalDate data, TipoMovimentacao tipo, Usuario usuario,
            Categoria categoria, boolean ativo) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.usuario_id = usuario;
        this.categoria_id = categoria;
        this.ativa = ativo;
    }
}