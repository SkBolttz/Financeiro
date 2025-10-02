package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        Categoria categoriaLocalizada = categoriaRepositorio
                .findById(movimentacaoDTO.categoria_id().getId())
                .orElseThrow(() -> new CategoriaNaoLocalizadaException(
                        "Categoria nao localizada.",
                        "Erro ao localizar a categoria, verifique se está cadastrada: "
                                + movimentacaoDTO.categoria_id().getId()));

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

        Categoria categoriaLocalizada = categoriaRepositorio
                .findById(movimentacaoDTO.categoria_id().getId())
                .orElseThrow(() -> new CategoriaNaoLocalizadaException(
                        "Categoria nao localizada.",
                        "Erro ao localizar a categoria, por favor, verifique se esta cadastrada"));

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

    public Page<Movimentacao> listarMovimentacao(int page, int size) {
        return movimentacaoRepositorio.findMovimentacoesMaisProximas(
                PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarReceitas(int page, int size) {
        return movimentacaoRepositorio.findByTipo("RECEITA", PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarDespesas(int page, int size) {
        return movimentacaoRepositorio.findByTipo("DESPESA", PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarReceitasAtivas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndAtiva("RECEITA", true, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarReceitasInativas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndAtiva("RECEITA", false, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarDespesasAtivas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndAtiva("DESPESA", true, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarDespesasInativas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndAtiva("DESPESA", false, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarDespesasPagas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndPago("DESPESA", true, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarDespesasAtrasadas(int page, int size) {
        return movimentacaoRepositorio.findByTipoAndAtrasado("DESPESA", true, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarMovimentacoesAtivas(int page, int size) {
        return movimentacaoRepositorio.findByAtivo(true, PageRequest.of(page, size));
    }

    // Funcionando
    public Page<Movimentacao> listarMovimentacoesInativas(int page, int size) {
        return movimentacaoRepositorio.findByAtivo(false, PageRequest.of(page, size));
    }
}
