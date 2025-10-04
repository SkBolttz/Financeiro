package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.CategoriaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.ErroGlobalCategoria;
import Sistema.Financeiro.Fincaneiro.Repositorio.CategoriaRepositorio;

@Service
public class CategoriaServico {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaServico(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    // Cadastrar
    public void cadastrarCategoria(Categoria categoria) {
        try {
            categoria.setAtiva(true);
            categoriaRepositorio.save(categoria);
        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao cadastrar categoria: ",
                    "Erro ao cadastrar a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    // Remover (desativar)
    public void removerCategoria(CategoriaDTO categoria) {
        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findByNomeAndUsuario(categoria.getNome(),
                    categoria.getUsuario());

            if (categoriaLocalizada == null) {
                throw new CategoriaNaoLocalizadaException("Categoria não localizada.",
                        "Erro ao localizar a categoria, por favor, verifique se está cadastrada");
            }

            categoriaLocalizada.setAtiva(false);
            categoriaRepositorio.save(categoriaLocalizada);

        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao remover categoria: ",
                    "Erro ao remover a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    // Editar
    public void editarCategoria(CategoriaDTO categoria) {
        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findById(categoria.getId())
                    .orElseThrow(() -> new CategoriaNaoLocalizadaException("Categoria não localizada.",
                            "Erro ao localizar a categoria, por favor, verifique se está cadastrada"));

            if (categoria.getNome() != null) {
                categoriaLocalizada.setNome(categoria.getNome());
            }

            if (categoria.getTipo() != null) {
                categoriaLocalizada.setTipo(categoria.getTipo());
            }

            categoriaRepositorio.save(categoriaLocalizada);

        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao editar categoria: ",
                    "Erro ao editar a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    // Listar todas de um usuário
    public List<Categoria> listarCategorias(Usuario usuario) {
        return categoriaRepositorio.findByUsuario(usuario);
    }

    // Listar ativas de um usuário
    public List<Categoria> listarCategoriasAtivas(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndAtiva(usuario, true);
    }

    // Listar inativas de um usuário
    public List<Categoria> listarCategoriasInativas(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndAtiva(usuario, false);
    }

    // Listar receitas de um usuário
    public List<Categoria> listarCategoriasReceita(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndTipoAndAtiva(usuario, TipoMovimentacao.RECEITA, true);
    }

    // Listar despesas de um usuário
    public List<Categoria> listarCategoriasDespesa(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndTipoAndAtiva(usuario, TipoMovimentacao.DESPESA, true);
    }
}
