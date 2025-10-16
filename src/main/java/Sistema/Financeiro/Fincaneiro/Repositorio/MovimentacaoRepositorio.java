package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.DTO.ExtratoMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoCategoriaDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoMensalDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoTipoPagamentoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.TopClientesDTO;
import Sistema.Financeiro.Fincaneiro.DTO.TopFornecedoresDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

@Repository
public interface MovimentacaoRepositorio extends JpaRepository<Movimentacao, Long> {

    // ------------------ Paginação ------------------
    Page<Movimentacao> findByUsuarioId(Long usuarioId, Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipo(Long usuarioId, TipoMovimentacao tipo, Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndAtiva(Long usuarioId, TipoMovimentacao tipo, boolean ativa,
            Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndAtiva(Long usuarioId, boolean ativa, Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndPago(Long usuarioId, TipoMovimentacao tipo, boolean pago,
            Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndAtrasado(Long usuarioId, TipoMovimentacao tipo, boolean atrasado,
            Pageable pageable);

    // ------------------ Queries customizadas ------------------
    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId ORDER BY m.data ASC")
    Page<Movimentacao> findMovimentacoesMaisProximas(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId AND m.ativa = :ativa ORDER BY m.data ASC")
    List<Movimentacao> findByUsuarioIdAndAtivaOrderByData(@Param("usuarioId") Long usuarioId,
            @Param("ativa") boolean ativa);

    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId AND m.tipo = :tipo AND m.ativa = :ativa ORDER BY m.data ASC")
    List<Movimentacao> findByUsuarioIdAndTipoAndAtivaOrderByData(@Param("usuarioId") Long usuarioId,
            @Param("tipo") TipoMovimentacao tipo, @Param("ativa") boolean ativa);

    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId AND m.tipo = :tipo AND m.data BETWEEN :inicio AND :fim AND m.ativa = :ativa")
    List<Movimentacao> findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
            @Param("usuarioId") long usuarioId,
            @Param("tipo") TipoMovimentacao tipo,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("ativa") boolean ativa);

    // ------------------ Filtrando por Categoria ------------------
    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId AND m.categoria_id.nome = :nomeCategoria AND m.ativa = :ativa")
    List<Movimentacao> findByUsuarioIdAndCategoriaNomeAndAtiva(
            @Param("usuarioId") long usuarioId,
            @Param("nomeCategoria") String nomeCategoria,
            @Param("ativa") boolean ativa);

    // ------------------ DTOs ------------------

    @Query("SELECT new Sistema.Financeiro.Fincaneiro.DTO.ExtratoMovimentacaoDTO(" +
            "m.descricao, m.categoria_id.nome, m.valor, m.data, m.tipo) " +
            "FROM Movimentacao m " +
            "WHERE m.usuario.id = :usuarioId AND m.ativa = :ativa")
    List<ExtratoMovimentacaoDTO> findExtratoByUsuarioIdAndAtiva(@Param("usuarioId") long usuarioId,
            @Param("ativa") boolean ativa);

    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId AND m.ativa = true AND m.tipo = :tipo AND m.pago = false")
    List<Movimentacao> buscarPorUsuarioAtivaTipoNaoPago(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") TipoMovimentacao tipo);

    List<Movimentacao> LancamentoRecorrente(boolean b);

    List<Movimentacao> findByLancamentoRecorrenteTrueAndAtivaTrue();

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.TopClientesDTO(
                    m.cliente.id,
                    m.cliente.nome,
                    COUNT(m)
                )
                FROM Movimentacao m
                WHERE m.usuario.id = :usuarioId
                AND m.tipo = 'RECEITA'
                AND m.cliente IS NOT NULL
                GROUP BY m.cliente.id, m.cliente.nome
                ORDER BY COUNT(m) DESC
            """)
    List<TopClientesDTO> findTopClientesPorMovimentacoes(@Param("usuarioId") long usuarioId);

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.TopFornecedoresDTO(
                    m.fornecedor.id,
                    m.fornecedor.razaoSocial,
                    COUNT(m)
                )
                FROM Movimentacao m
                WHERE m.usuario.id = :usuarioId
                AND m.tipo = 'DESPESA'
                AND m.fornecedor IS NOT NULL
                GROUP BY m.fornecedor.id, m.fornecedor.razaoSocial
                ORDER BY COUNT(m) DESC
            """)
    List<TopFornecedoresDTO> findTopFornecedoresPorDespesas(@Param("usuarioId") long usuarioId);

    @Query("""
                SELECT SUM(m.valor)
                FROM Movimentacao m
                WHERE m.tipo = :tipo
                AND m.usuario.id = :usuarioId
            """)
    Double calcularSomaPorTipo(@Param("tipo") TipoMovimentacao tipo, @Param("usuarioId") long usuarioId);

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoCategoriaDTO(
                    m.categoria_id.nome,
                    SUM(m.valor),
                    m.tipo
                )
                FROM Movimentacao m
                WHERE m.usuario.id = :id
                GROUP BY m.categoria_id.nome, m.tipo
                ORDER BY SUM(m.valor) DESC
            """)
    List<MovimentacaoCategoriaDTO> findTotaisPorCategoria(@Param("id") long id);

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoMensalDTO(
                    MONTH(m.data),
                    SUM(m.valor),
                    m.tipo
                )
                FROM Movimentacao m
                WHERE m.usuario.id = :id
                GROUP BY MONTH(m.data), m.tipo
                ORDER BY MONTH(m.data)
            """)
    List<MovimentacaoMensalDTO> findTotaisPorMes(@Param("id") long id);

    @Query("""
                SELECT m
                FROM Movimentacao m
                WHERE m.usuario.id = :id
                AND m.lancamentoRecorrente = true
                ORDER BY m.data DESC
            """)
    Collection<Movimentacao> findRecorrentesPorUsuario(@Param("id") long id);

    @Query("""
                SELECT new Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoTipoPagamentoDTO(
                    m.tipoPagamento,
                    SUM(m.valor),
                    m.tipo
                )
                FROM Movimentacao m
                WHERE m.usuario.id = :id
                GROUP BY m.tipoPagamento, m.tipo
                ORDER BY SUM(m.valor) DESC
            """)
    List<MovimentacaoTipoPagamentoDTO> findTotaisPorTipoPagamento(@Param("id") long id);

}
