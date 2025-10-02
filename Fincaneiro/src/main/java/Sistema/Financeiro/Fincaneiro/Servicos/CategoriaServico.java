package Sistema.Financeiro.Fincaneiro.Servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.CategoriaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaCadastradaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.ErroGlobalCategoria;
import Sistema.Financeiro.Fincaneiro.Repositorio.CategoriaRepositorio;

@Service
public class CategoriaServico {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaServico(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    // Funcionando
    public void cadastrarCategoria(Categoria categoria) {

        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findByNome(categoria.getNome());
            if (categoriaLocalizada != null) {
                throw new CategoriaCadastradaException("Categoria ja cadastrada.",
                        "Erro ao cadastrar a categoria, verifique se a categoria ja foi cadastrada.");
            }
            categoria.setAtiva(true);
            categoriaRepositorio.save(categoria);
        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao cadastrar categoria: ",
                    "Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void removerCategoria(CategoriaDTO categoria) {

        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findByNome(categoria.nome());

            if (categoriaLocalizada == null) {
                throw new CategoriaNaoLocalizadaException("Categoria nao localizada.",
                        "Erro ao localizar a categoria, por favor, verique se esta cadastrada");
            }

            categoriaLocalizada.setAtiva(false);
            categoriaRepositorio.save(categoriaLocalizada);

        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao cadastrar categoria: ",
                    "Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void editarCategoria(CategoriaDTO categoria) {

        try {
            Categoria categoriaLocalizada = categoriaRepositorio.findById(categoria.id())
                    .orElseThrow(() -> new CategoriaNaoLocalizadaException("Categoria nao localizada.",
                            "Erro ao localizar a categoria, por favor, verique se esta cadastrada"));

            if (categoria.nome() != null) {
                categoriaLocalizada.setNome(categoria.nome());
            }

            if (categoria.tipo() != null) {
                categoriaLocalizada.setTipo(categoria.tipo());
            }

            categoriaRepositorio.save(categoriaLocalizada);

        } catch (Exception e) {
            throw new ErroGlobalCategoria("Erro ao cadastrar categoria: ",
                    "Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }

    // Funcionando
    public List<Categoria> listarCategoriasAtivas() {
        return categoriaRepositorio.findByAtiva(true);
    }

    // Funcionando
    public List<Categoria> listarCategoriasInativas() {
        return categoriaRepositorio.findByAtiva(false);
    }

    // Funcionando
    public List<Categoria> listarCategoriasReceita() {
        return categoriaRepositorio.findByTipoAndAtiva(TipoMovimentacao.RECEITA, true);
    }

    public List<Categoria> listarCategoriasDespesa() {
        return categoriaRepositorio.findByTipoAndAtiva(TipoMovimentacao.DESPESA, true);
    }
}
