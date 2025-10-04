package Sistema.Financeiro.Fincaneiro.Repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    Categoria findByNomeAndUsuario(String nome, Usuario usuario);

    List<Categoria> findByUsuario(Usuario usuario);

    List<Categoria> findByUsuarioAndAtiva(Usuario usuario, boolean ativa);

    List<Categoria> findByUsuarioAndTipo(Usuario usuario, TipoMovimentacao tipo);

    List<Categoria> findByUsuarioAndTipoAndAtiva(Usuario usuario, TipoMovimentacao tipo, boolean ativa);
}
