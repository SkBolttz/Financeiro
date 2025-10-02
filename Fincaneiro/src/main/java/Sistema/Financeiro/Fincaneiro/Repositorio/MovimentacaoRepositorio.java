package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;

@Repository
public interface MovimentacaoRepositorio extends JpaRepository<Movimentacao, Long> {

    Page<Movimentacao> findByTipo(String string, PageRequest pageRequest);

    Page<Movimentacao> findByTipoAndAtiva(String string, boolean b, PageRequest pageRequest);

    Page<Movimentacao> findByTipoAndPago(String string, boolean b, PageRequest pageRequest);

    Page<Movimentacao> findByTipoAndAtrasado(String string, boolean b, PageRequest pageRequest);

    @Query("SELECT m FROM Movimentacao m ORDER BY m.data ASC")
    Page<Movimentacao> findMovimentacoesMaisProximas(Pageable pageable);

    Page<Movimentacao> findByAtivo(boolean b, PageRequest of);
}
