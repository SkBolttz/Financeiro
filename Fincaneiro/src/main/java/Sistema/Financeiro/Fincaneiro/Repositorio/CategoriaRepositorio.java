package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

    Categoria findByNome(String descricao);

    List<Categoria> findByAtiva(boolean b);

    List<Categoria> findByTipo(String string);

    List<Categoria> findByTipoAndAtiva(TipoMovimentacao tipo, boolean ativa);

}
