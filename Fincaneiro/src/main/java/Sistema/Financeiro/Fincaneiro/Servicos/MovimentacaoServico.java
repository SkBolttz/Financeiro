package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<Movimentacao> listarMovimentacao(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findMovimentacoesMaisProximas(usuarioId, PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarReceitas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipo(usuarioId, TipoMovimentacao.RECEITA,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarDespesas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipo(usuarioId, TipoMovimentacao.DESPESA,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarReceitasAtivas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndAtiva(usuarioId, TipoMovimentacao.RECEITA, true,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarReceitasInativas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndAtiva(usuarioId, TipoMovimentacao.RECEITA, false,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarDespesasAtivas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndAtiva(usuarioId, TipoMovimentacao.DESPESA, true,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarDespesasInativas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndAtiva(usuarioId, TipoMovimentacao.DESPESA, false,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarDespesasPagas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndPago(usuarioId, TipoMovimentacao.DESPESA, true,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarDespesasAtrasadas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndAtrasado(usuarioId, TipoMovimentacao.DESPESA, true,
                PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarMovimentacoesAtivas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndAtiva(usuarioId, true, PageRequest.of(page, size));
    }

    public Page<Movimentacao> listarMovimentacoesInativas(Long usuarioId, int page, int size) {
        return movimentacaoRepositorio.findByUsuarioIdAndAtiva(usuarioId, false, PageRequest.of(page, size));
    }

    public List<Movimentacao> verificarPertoVencimento(long id) {

        List<Movimentacao> movimentacaos = movimentacaoRepositorio.findByUsuarioIdAndAtivaAndTipoAndPago(id, true,
                TipoMovimentacao.DESPESA, false);

        LocalDate amanha = LocalDate.now().plusDays(1);

        return movimentacaos.stream()
                .filter(m -> m.getData().isEqual(amanha))
                .collect(Collectors.toList());
    }

    public List<Movimentacao> alterarDespesaVencida(long id) {

        List<Movimentacao> movimentacaos = movimentacaoRepositorio.findByUsuarioIdAndAtivaAndTipoAndPago(id, true,
                TipoMovimentacao.DESPESA, false);

        List<Movimentacao> vencidas = movimentacaos.stream()
                .filter(m -> m.getData().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        vencidas.forEach(m -> {
            System.out.println("Alerta para " + m.getUsuario().getNome() + ": despesa " + m.getDescricao()
                    + " Vencida (" + m.getData() + ")");
            m.setAtrasado(true);
            movimentacaoRepositorio.save(m);
        });

        return vencidas;
    }

    public Movimentacao alterarMovimentacaoPaga(AlterarMovimentacaoDTO movimentacaoDTO) {

        Movimentacao movLocalizada = movimentacaoRepositorio.findById(movimentacaoDTO.id())
                .orElseThrow(() -> new MovimentacaoNaoLocalizadaException("Movimentação nao localizada",
                        "Movimentação nao localizada, favor criar uma."));

        if (movLocalizada.getPago() == false) {
            movLocalizada.setPago(true);
        } else {
            movLocalizada.setPago(false);
        }

        return movimentacaoRepositorio.save(movLocalizada);
    }
}
