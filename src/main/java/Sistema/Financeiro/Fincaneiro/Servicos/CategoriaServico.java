package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.CategoriaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaDuplicadaException;
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
            boolean existe = categoriaRepositorio.existsByNomeAndTipoAndUsuario(
                    categoria.getNome(),
                    categoria.getTipo(),
                    categoria.getUsuario());

            if (existe) {
                throw new CategoriaDuplicadaException("Categoria duplicada",
                        "Já existe uma categoria com esse nome e tipo para o usuário informado.");
            }

            categoria.setAtiva(true);
            categoriaRepositorio.save(categoria);

        } catch (CategoriaDuplicadaException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao cadastrar categoria: ",
                    "Erro ao cadastrar a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    public void removerCategoria(CategoriaDTO categoria) {
        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findByNomeAndUsuario(
                    categoria.getNome(),
                    categoria.getUsuario());

            if (categoriaLocalizada == null) {
                throw new CategoriaNaoLocalizadaException("Categoria não localizada.",
                        "Erro ao localizar a categoria, verifique se está cadastrada.");
            }

            categoriaLocalizada.setAtiva(false);
            categoriaRepositorio.save(categoriaLocalizada);

        } catch (CategoriaNaoLocalizadaException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao remover categoria: ",
                    "Erro ao remover a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    public void editarCategoria(CategoriaDTO categoriaDTO) {
        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findById(categoriaDTO.getId())
                    .orElseThrow(() -> new CategoriaNaoLocalizadaException("Categoria não localizada.",
                            "Erro ao localizar a categoria, verifique se está cadastrada."));

            boolean existe = categoriaRepositorio.existsByNomeAndTipoAndUsuarioAndIdNot(
                    categoriaDTO.getNome() != null ? categoriaDTO.getNome() : categoriaLocalizada.getNome(),
                    categoriaDTO.getTipo() != null ? categoriaDTO.getTipo() : categoriaLocalizada.getTipo(),
                    categoriaLocalizada.getUsuario(),
                    categoriaLocalizada.getId());

            if (existe) {
                throw new CategoriaDuplicadaException("Categoria duplicada",
                        "Já existe uma categoria com esse nome e tipo para o usuário informado.");
            }

            if (categoriaDTO.getNome() != null) {
                categoriaLocalizada.setNome(categoriaDTO.getNome());
            }

            if (categoriaDTO.getTipo() != null) {
                categoriaLocalizada.setTipo(categoriaDTO.getTipo());
            }

            categoriaRepositorio.save(categoriaLocalizada);

        } catch (CategoriaNaoLocalizadaException | CategoriaDuplicadaException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao editar categoria: ",
                    "Erro ao editar a categoria, por favor, valide o erro: " + e.getMessage());
        }
    }

    public List<Categoria> listarCategorias(Usuario usuario) {
        return categoriaRepositorio.findByUsuario(usuario);
    }

    public List<Categoria> listarCategoriasAtivas(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndAtiva(usuario, true);
    }

    public List<Categoria> listarCategoriasInativas(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndAtiva(usuario, false);
    }

    public List<Categoria> listarCategoriasReceita(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndTipoAndAtiva(usuario, TipoMovimentacao.RECEITA, true);
    }

    public List<Categoria> listarCategoriasDespesa(Usuario usuario) {
        return categoriaRepositorio.findByUsuarioAndTipoAndAtiva(usuario, TipoMovimentacao.DESPESA, true);
    }
}
