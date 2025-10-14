package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Enum.Periodicidade;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarMovimentacaoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long categoriaId;
    private String categoriaNome;
    private TipoMovimentacao tipo;
    private Double valor;
    private LocalDate data;
    private String descricao;
    private Boolean pago;
    private Boolean atrasado;
    private Boolean ativa;
    private LocalDate dataDeDesativacao;
    private LocalDate dataDePagamento;
    private TipoPagamento tipoPagamento;
    private Boolean lancamentoRecorrente;
    private LocalDate dataLancamentoRecorrenteCriacao;
    private LocalDate dataLancamentoRecorrenteProxima;
    private Periodicidade periodicidade;
    private LocalDate dataFimRecorrencia;
    private Integer totalRecorrencias;
    private Integer recorrenciasCriadas;
    private String comprovanteEntrada;
    private String comprovanteRestante;
    private Cliente clienteSelecionado;
    private Fornecedor fornecedorSelecionado;
}
