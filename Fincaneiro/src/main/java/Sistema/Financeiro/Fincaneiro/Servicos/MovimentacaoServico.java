package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RemoverMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaCadastradaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaIncorretaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.ErroGlobalMovimentacaoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.MovimentacaoInativaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.MovimentacaoNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.TipoIncorretoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Usuario.UsuarioNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Repositorio.CategoriaRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class MovimentacaoServico {

    private final MovimentacaoRepositorio movimentacaoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;

    public MovimentacaoServico(MovimentacaoRepositorio movimentacaoRepositorio, UsuarioRepositorio usuarioRepositorio,
            CategoriaRepositorio categoriaRepositorio) {
        this.movimentacaoRepositorio = movimentacaoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
    }

    // Funcionando
    @Transactional
    public void adicionarReceita(@Valid MovimentacaoDTO movimentacaoDTO) {
        Usuario usuarioLocalizado = usuarioRepositorio.findById(movimentacaoDTO.usuario_id().getId())
                .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário não localizado.",
                        "Usuário nao localizado, favor criar um."));

        if (movimentacaoDTO.tipo() != TipoMovimentacao.RECEITA) {
            throw new TipoIncorretoException("Tipo de incorreto!", "Tipo de movimentação inválido, esperado RECEITA.");
        }

        Categoria categoriaLocalizada = categoriaRepositorio.findByNome(movimentacaoDTO.categoria_nome().getNome());
        if (categoriaLocalizada == null) {
            throw new CategoriaNaoLocalizadaException("Categoria nao localizada.",
                    "Erro ao localizar a categoria, por favor, verique se esta cadastrada");
        }

        if (categoriaLocalizada.getTipo() != TipoMovimentacao.RECEITA) {
            throw new CategoriaIncorretaException("Categoria incorreta!", "Categoria nao e uma categoria de receita.");
        }

        usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() + movimentacaoDTO.valor());
        usuarioRepositorio.save(usuarioLocalizado);

        Movimentacao movimentacao = new Movimentacao(
                movimentacaoDTO.descricao(),
                movimentacaoDTO.valor(),
                movimentacaoDTO.data(),
                movimentacaoDTO.tipo(),
                usuarioLocalizado,
                categoriaLocalizada,
                true);

        movimentacaoRepositorio.save(movimentacao);
    }

    // Funcionando
    public void removerReceita(RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            Movimentacao movimentacao = movimentacaoRepositorio.findById(movimentacaoDTO.id())
                    .orElseThrow(() -> new MovimentacaoNaoLocalizadaException("Movimentação não localizada.",
                            "Movimentação nao localizada, favor criar uma."));

            Usuario usuarioLocalizado = usuarioRepositorio.findById(movimentacaoDTO.usuario().getId())
                    .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário nao localizado.",
                            "Usuário nao localizado, favor criar um."));

            usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() - movimentacao.getValor());
            usuarioRepositorio.save(usuarioLocalizado);

            movimentacao.setAtiva(false);
            movimentacao.setDataDeDesativacao(LocalDate.now());
            movimentacaoRepositorio.save(movimentacao);

        } catch (ErroGlobalMovimentacaoException e) {
            throw new ErroGlobalMovimentacaoException("Erro ao remover movimentação:",
                    "Erro ao remover receita, por favor, verifique o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void adicionarDespesa(MovimentacaoDTO movimentacaoDTO) {
        Usuario usuarioLocalizado = usuarioRepositorio.findById(movimentacaoDTO.usuario_id().getId())
                .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário não localizado.",
                        "Usuário nao localizado, favor criar um."));

        if (movimentacaoDTO.tipo() != TipoMovimentacao.DESPESA) {
            throw new TipoIncorretoException("Tipo de incorreto!", "Tipo de movimentação inválido, esperado DESPESA.");
        }

        Categoria categoriaLocalizada = categoriaRepositorio.findByNome(movimentacaoDTO.categoria_nome().getNome());
        if (categoriaLocalizada == null) {
            throw new CategoriaNaoLocalizadaException("Categoria nao localizada.",
                    "Erro ao localizar a categoria, por favor, verique se esta cadastrada");
        }

        if (categoriaLocalizada.getTipo() != TipoMovimentacao.DESPESA) {
            throw new CategoriaIncorretaException("Categoria incorreta!", "Categoria nao e uma categoria de despesa.");
        }

        usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() - movimentacaoDTO.valor());
        usuarioRepositorio.save(usuarioLocalizado);

        Movimentacao movimentacao = new Movimentacao(
                movimentacaoDTO.descricao(),
                movimentacaoDTO.valor(),
                movimentacaoDTO.data(),
                movimentacaoDTO.tipo(),
                usuarioLocalizado,
                categoriaLocalizada,
                true);

                movimentacao.setPago(false);

        movimentacaoRepositorio.save(movimentacao);
    }

    // Funcionando
    public void removerDespesa(RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            Movimentacao movimentacao = movimentacaoRepositorio.findById(movimentacaoDTO.id())
                    .orElseThrow(() -> new MovimentacaoNaoLocalizadaException("Movimentação não localizada.",
                            "Movimentação nao localizada, favor criar uma."));

            Usuario usuarioLocalizado = usuarioRepositorio.findById(movimentacaoDTO.usuario().getId())
                    .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário nao localizado.",
                            "Usuário nao localizado, favor criar um."));

            usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() + movimentacao.getValor());
            usuarioRepositorio.save(usuarioLocalizado);

            movimentacao.setAtiva(false);
            movimentacao.setDataDeDesativacao(LocalDate.now());
            movimentacaoRepositorio.save(movimentacao);

        } catch (ErroGlobalMovimentacaoException e) {
            throw new ErroGlobalMovimentacaoException("Erro ao remover movimentação:",
                    "Erro ao remover despesa, por favor, verifique o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void editarReceita(AlterarMovimentacaoDTO movimentacaoDTO) {
        try {
            Movimentacao movimentacaoLocalizada = movimentacaoRepositorio.findById(movimentacaoDTO.id())
                    .orElseThrow(() -> new MovimentacaoNaoLocalizadaException(
                            "Movimentação não localizada.",
                            "Movimentação não localizada, favor criar uma."));

            if (!movimentacaoLocalizada.isAtiva()) {
                throw new MovimentacaoInativaException(
                        "Erro ao atualizar movimentação",
                        "Movimentação encontra-se inativa, favor validar.");
            }

            if (movimentacaoDTO.descricao() != null && !movimentacaoDTO.descricao().isBlank()) {
                movimentacaoLocalizada.setDescricao(movimentacaoDTO.descricao());
            }

            if (movimentacaoDTO.data() != null) {
                movimentacaoLocalizada.setData(movimentacaoDTO.data());
            }

            if (movimentacaoDTO.valor() != null) {
                movimentacaoLocalizada.setValor(movimentacaoDTO.valor());
            }

            if (movimentacaoDTO.tipo() != null) {
                movimentacaoLocalizada.setTipo(movimentacaoDTO.tipo());
            }

            if (movimentacaoDTO.categoria_id() != null) {
                Categoria categoriaLocalizada = categoriaRepositorio.findById(movimentacaoDTO.categoria_id().getId())
                        .orElseThrow(() -> new CategoriaCadastradaException(
                                "Categoria não localizada",
                                "Favor validar se a categoria está cadastrada."));

                movimentacaoLocalizada.setCategoria_id(categoriaLocalizada);
            }

            movimentacaoRepositorio.save(movimentacaoLocalizada);

        } catch (ErroGlobalMovimentacaoException e) {
            throw new ErroGlobalMovimentacaoException(
                    "Erro ao editar receita",
                    "Erro ao atualizar receita, verifique o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void editarDespesa(AlterarMovimentacaoDTO movimentacaoDTO) {
        try {
            Movimentacao movimentacaoLocalizada = movimentacaoRepositorio.findById(movimentacaoDTO.id())
                    .orElseThrow(() -> new MovimentacaoNaoLocalizadaException(
                            "Movimentação não localizada.",
                            "Movimentação não localizada, favor criar uma."));

            if (!movimentacaoLocalizada.isAtiva()) {
                throw new MovimentacaoInativaException(
                        "Erro ao atualizar movimentação",
                        "Movimentação encontra-se inativa, favor validar.");
            }

            if (movimentacaoDTO.descricao() != null && !movimentacaoDTO.descricao().isBlank()) {
                movimentacaoLocalizada.setDescricao(movimentacaoDTO.descricao());
            }

            if (movimentacaoDTO.data() != null) {
                movimentacaoLocalizada.setData(movimentacaoDTO.data());
            }

            if (movimentacaoDTO.valor() != null) {
                movimentacaoLocalizada.setValor(movimentacaoDTO.valor());
            }

            if (movimentacaoDTO.tipo() != null) {
                movimentacaoLocalizada.setTipo(movimentacaoDTO.tipo());
            }

            if (movimentacaoDTO.categoria_id() != null) {
                Categoria categoriaLocalizada = categoriaRepositorio.findById(movimentacaoDTO.categoria_id().getId())
                        .orElseThrow(() -> new CategoriaCadastradaException(
                                "Categoria não localizada",
                                "Favor validar se a categoria está cadastrada."));

                movimentacaoLocalizada.setCategoria_id(categoriaLocalizada);
            }

            movimentacaoRepositorio.save(movimentacaoLocalizada);

        } catch (ErroGlobalMovimentacaoException e) {
            throw new ErroGlobalMovimentacaoException(
                    "Erro ao editar despesa",
                    "Erro ao atualizar despesa, verifique o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public List<Movimentacao> listarMovimentacao() {
        return movimentacaoRepositorio.findAll();
    }

    // Funcionando
    public List<Movimentacao> listarReceitas() {
        return movimentacaoRepositorio.findByTipo("RECEITA");
    }

    // Funcionando
    public List<Movimentacao> listarDespesas() {
        return movimentacaoRepositorio.findByTipo("DESPESA");
    }

    // Funcionando
    public List<Movimentacao> listarReceitasAtivas() {
        return movimentacaoRepositorio.findByTipoAndAtiva("RECEITA", true);
    }

    // Funcionando
    public List<Movimentacao> listarReceitasInativas() {
        return movimentacaoRepositorio.findByTipoAndAtiva("RECEITA", false);
    }

    // Funcionando
    public List<Movimentacao> listarDespesasAtivas() {
        return movimentacaoRepositorio.findByTipoAndAtiva("DESPESA", true);
    }

    // Funcionando
    public List<Movimentacao> listarDespesasInativas() {
        return movimentacaoRepositorio.findByTipoAndAtiva("DESPESA", false);
    }

    // Funcionando
    public List<Movimentacao> listarDespesasPagas() {
        return movimentacaoRepositorio.findByTipoAndPago("DESPESA", true);
    }

    // Funcionando
    public List<Movimentacao> listarDespesasAtrasadas() {
        return movimentacaoRepositorio.findByTipoAndAtrasado("DESPESA", true);
    }
}
