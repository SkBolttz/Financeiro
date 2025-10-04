package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.DTO.ExtratoMovimentacaoDTO;
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
}
