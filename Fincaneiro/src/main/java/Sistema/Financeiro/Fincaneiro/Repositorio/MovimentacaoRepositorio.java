package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;

@Repository
public interface MovimentacaoRepositorio extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByTipo(String string);

    List<Movimentacao> findByTipoAndAtiva(String string, boolean b);

    List<Movimentacao> findByTipoAndPago(String string, boolean b);

    List<Movimentacao> findByTipoAndAtrasado(String string, boolean b);

}
