package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

@Repository
public interface MovimentacaoRepositorio extends JpaRepository<Movimentacao, Long> {
    Page<Movimentacao> findByUsuarioId(Long usuarioId, Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipo(Long usuarioId, TipoMovimentacao tipo, Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndAtiva(Long usuarioId, TipoMovimentacao tipo, boolean ativa,
            Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndPago(Long usuarioId, TipoMovimentacao tipo, boolean pago,
            Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndTipoAndAtrasado(Long usuarioId, TipoMovimentacao tipo, boolean atrasado,
            Pageable pageable);

    Page<Movimentacao> findByUsuarioIdAndAtiva(Long usuarioId, boolean ativa, Pageable pageable);

    @Query("SELECT m FROM Movimentacao m WHERE m.usuario.id = :usuarioId ORDER BY m.data ASC")
    Page<Movimentacao> findMovimentacoesMaisProximas(@Param("usuarioId") Long usuarioId, Pageable pageable);

    Movimentacao findByUsuarioIdAndTipo(long id, TipoMovimentacao despesa);

    Movimentacao findByUsuarioIdAndTipoAndTipoAndAtiva(long id, TipoMovimentacao despesa, TipoMovimentacao despesa2,
                boolean b);

    List<Movimentacao> findByUsuarioIdAndTipoAndDataBetweenAndAtiva(long id, TipoMovimentacao despesa,
            LocalDate umAnoAtras, LocalDate hoje, boolean b);
}
