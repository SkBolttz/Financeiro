package Sistema.Financeiro.Fincaneiro.Entidade;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Enum.Periodicidade;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoPagamento;
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
    @JoinColumn(nullable = false)
    private Usuario usuario;

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

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    private Boolean lancamentoRecorrente;
    private LocalDate dataLancamentoRecorrenteCriacao;
    private LocalDate dataLancamentoRecorrenteProxima;

    @Enumerated(EnumType.STRING)
    private Periodicidade periodicidade;

    private LocalDate dataFimRecorrencia;
    private Integer totalRecorrencias;
    private Integer recorrenciasCriadas;

    private String comprovanteEntrada;
    private String comprovanteRestante;

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Fornecedor fornecedor;

    public Movimentacao(String descricao, Double valor, LocalDate data, TipoMovimentacao tipo, Usuario usuario,
            Categoria categoria, boolean ativo, Periodicidade periodicidade2, LocalDate localDate, Integer integer, TipoPagamento tipoPagamento2, Cliente cliente2, Fornecedor fornecedor2) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.usuario = usuario;
        this.categoria_id = categoria;
        this.ativa = ativo;
        this.periodicidade = periodicidade2;
        this.dataFimRecorrencia = localDate;
        this.totalRecorrencias = integer;
        this.tipoPagamento = tipoPagamento2;
        this.cliente = cliente2;
        this.fornecedor = fornecedor2;
    }
}
