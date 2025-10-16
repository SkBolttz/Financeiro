package Sistema.Financeiro.Fincaneiro.Servicos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RemoverMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Cliente;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
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
import Sistema.Financeiro.Fincaneiro.Repositorio.ClienteRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.FornecedorRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import jakarta.transaction.Transactional;

@Service
public class MovimentacaoServico {

    private final MovimentacaoRepositorio movimentacaoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final FornecedorRepositorio fornecedorRepositorio;

    public MovimentacaoServico(MovimentacaoRepositorio movimentacaoRepositorio, UsuarioRepositorio usuarioRepositorio,
            CategoriaRepositorio categoriaRepositorio, ClienteRepositorio clienteRepositorio,
            FornecedorRepositorio fornecedorRepositorio) {
        this.movimentacaoRepositorio = movimentacaoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.fornecedorRepositorio = fornecedorRepositorio;
    }

    // Funcionando
    @Transactional
    public void adicionarReceita(MovimentacaoDTO dto, MultipartFile comprovanteEntrada,
            MultipartFile comprovanteRestante) {
        Usuario usuario = usuarioRepositorio.findById(dto.usuario_id().getId())
                .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário não localizado.",
                        "Usuário nao localizado, favor criar um."));

        if (dto.tipo() != TipoMovimentacao.RECEITA)
            throw new TipoIncorretoException("Tipo de movimentação inválido, esperado RECEITA.",
                    "Tipo de movimentação inválido, esperado RECEITA.");

        Categoria categoria = categoriaRepositorio.findById(dto.categoria_id().getId())
                .orElseThrow(() -> new CategoriaNaoLocalizadaException("Categoria não localizada.",
                        "Categoria nao localizada, favor criar uma."));

        if (categoria.getTipo() != TipoMovimentacao.RECEITA)
            throw new CategoriaIncorretaException("Categoria não é de receita.", "Categoria nao é de receita.");

        usuario.setSaldo(usuario.getSaldo() + dto.valor());
        usuarioRepositorio.save(usuario);

        Cliente clienteSelecionado = null;
        if (dto.cliente() != null && dto.cliente().getId() != null) {
            clienteSelecionado = clienteRepositorio.findById(dto.cliente().getId())
                    .orElse(null);
        }

        Fornecedor fornecedorSelecionado = null;
        if (dto.fornecedor() != null && dto.fornecedor().getId() != 0L) {
            fornecedorSelecionado = fornecedorRepositorio.findById(dto.fornecedor().getId())
                    .orElse(null);
        }

        Movimentacao movimentacao = new Movimentacao(
                dto.descricao(),
                dto.valor(),
                dto.data(),
                dto.tipo(),
                usuario,
                categoria,
                true,
                dto.periodicidade(),
                dto.dataFimRecorrencia(),
                dto.totalRecorrencias(),
                dto.tipoPagamento(),
                clienteSelecionado,
                fornecedorSelecionado);

        movimentacao.setPago(false);

        if (dto.lancamentoRecorrente() != null && dto.lancamentoRecorrente()) {
            movimentacao.setDataLancamentoRecorrenteCriacao(LocalDate.now());
            movimentacao.setDataLancamentoRecorrenteProxima(LocalDate.now().plusMonths(1));
        }

        if (comprovanteEntrada != null && !comprovanteEntrada.isEmpty()) {
            String caminhoEntrada = salvarArquivo(comprovanteEntrada);
            movimentacao.setComprovanteEntrada(caminhoEntrada);
        }
        if (comprovanteRestante != null && !comprovanteRestante.isEmpty()) {
            String caminhoRestante = salvarArquivo(comprovanteRestante);
            movimentacao.setComprovanteRestante(caminhoRestante);
        }

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
    @Transactional
    public void adicionarDespesa(MovimentacaoDTO dto, MultipartFile comprovanteEntrada,
            MultipartFile comprovanteRestante) {
        Usuario usuario = usuarioRepositorio.findById(dto.usuario_id().getId())
                .orElseThrow(() -> new UsuarioNaoLocalizadoException("Usuário não localizado.",
                        "Usuário nao localizado, favor criar um."));

        if (dto.tipo() != TipoMovimentacao.DESPESA)
            throw new TipoIncorretoException("Tipo de movimentação inválido, esperado DESPESA.",
                    "Tipo de movimentação inválido, esperado DESPESA.");

        Categoria categoria = categoriaRepositorio.findById(dto.categoria_id().getId())
                .orElseThrow(() -> new CategoriaNaoLocalizadaException("Categoria não localizada.",
                        "Categoria nao localizada, favor criar uma."));

        if (categoria.getTipo() != TipoMovimentacao.DESPESA)
            throw new CategoriaIncorretaException("Categoria não é de despesa.", "Categoria nao é de despesa.");

        usuario.setSaldo(usuario.getSaldo() - dto.valor());
        usuarioRepositorio.save(usuario);

        Cliente clienteSelecionado = null;
        if (dto.cliente() != null && dto.cliente().getId() != null) {
            clienteSelecionado = clienteRepositorio.findById(dto.cliente().getId())
                    .orElse(null);
        }

        Fornecedor fornecedorSelecionado = null;
        if (dto.fornecedor() != null && dto.fornecedor().getId() != 0L) {
            fornecedorSelecionado = fornecedorRepositorio.findById(dto.fornecedor().getId())
                    .orElse(null);
        }

        Movimentacao movimentacao = new Movimentacao(
                dto.descricao(),
                dto.valor(),
                dto.data(),
                dto.tipo(),
                usuario,
                categoria,
                true,
                dto.periodicidade(),
                dto.dataFimRecorrencia(),
                dto.totalRecorrencias(),
                dto.tipoPagamento(),
                clienteSelecionado,
                fornecedorSelecionado);

        movimentacao.setPago(false);

        if (comprovanteEntrada != null && !comprovanteEntrada.isEmpty()) {
            String caminhoEntrada = salvarArquivo(comprovanteEntrada);
            movimentacao.setComprovanteEntrada(caminhoEntrada);
        }
        if (comprovanteRestante != null && !comprovanteRestante.isEmpty()) {
            String caminhoRestante = salvarArquivo(comprovanteRestante);
            movimentacao.setComprovanteRestante(caminhoRestante);
        }

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
    public void editarReceita(AlterarMovimentacaoDTO movimentacaoDTO,
            MultipartFile comprovanteEntrada,
            MultipartFile comprovanteRestante) {
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

            // Atualização de campos básicos
            if (movimentacaoDTO.descricao() != null && !movimentacaoDTO.descricao().isBlank()) {
                movimentacaoLocalizada.setDescricao(movimentacaoDTO.descricao());
            }
            if (movimentacaoDTO.data() != null) {
                movimentacaoLocalizada.setData(movimentacaoDTO.data());
            }
            if (movimentacaoDTO.valor() != null) {
                if (movimentacaoDTO.valor() != null) {
                    double valorAntigo = movimentacaoLocalizada.getValor();
                    double valorNovo = movimentacaoDTO.valor();
                    double diferenca = valorNovo - valorAntigo;

                    movimentacaoLocalizada.setValor(valorNovo);
                    Usuario usuarioLocalizado = movimentacaoLocalizada.getUsuario();
                    usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() + diferenca);
                    usuarioRepositorio.save(usuarioLocalizado);
                }
            }
            if (movimentacaoDTO.tipo() != null) {
                movimentacaoLocalizada.setTipo(movimentacaoDTO.tipo());
            }
            if (movimentacaoDTO.tipoPagamento() != null) {
                movimentacaoLocalizada.setTipoPagamento(movimentacaoDTO.tipoPagamento());
            }
            if (movimentacaoDTO.lancamentoRecorrente() != null) {
                movimentacaoLocalizada.setLancamentoRecorrente(movimentacaoDTO.lancamentoRecorrente());
            }
            if (movimentacaoDTO.periodicidade() != null) {
                movimentacaoLocalizada.setPeriodicidade(movimentacaoDTO.periodicidade());
            }
            if (movimentacaoDTO.dataFimRecorrencia() != null) {
                movimentacaoLocalizada.setDataFimRecorrencia(movimentacaoDTO.dataFimRecorrencia());
            }
            if (movimentacaoDTO.totalRecorrencias() != null) {
                movimentacaoLocalizada.setTotalRecorrencias(movimentacaoDTO.totalRecorrencias());
            }

            if (movimentacaoDTO.cliente() != null) {
                movimentacaoLocalizada.setCliente(movimentacaoDTO.cliente());
            }

            if (movimentacaoDTO.fornecedor() != null) {
                movimentacaoLocalizada.setFornecedor(movimentacaoDTO.fornecedor());
            }

            // Atualizar categoria
            if (movimentacaoDTO.categoria_id() != null) {
                Categoria categoriaLocalizada = categoriaRepositorio.findById(movimentacaoDTO.categoria_id().getId())
                        .orElseThrow(() -> new CategoriaCadastradaException(
                                "Categoria não localizada",
                                "Favor validar se a categoria está cadastrada."));
                movimentacaoLocalizada.setCategoria_id(categoriaLocalizada);
            }

            // Atualizar comprovantes (arquivos ou string base64 / path)
            if (comprovanteEntrada != null && !comprovanteEntrada.isEmpty()) {
                String caminhoEntrada = salvarArquivo(comprovanteEntrada);
                movimentacaoLocalizada.setComprovanteEntrada(caminhoEntrada);
            }
            if (comprovanteRestante != null && !comprovanteRestante.isEmpty()) {
                String caminhoRestante = salvarArquivo(comprovanteRestante);
                movimentacaoLocalizada.setComprovanteRestante(caminhoRestante);
            }

            if (movimentacaoDTO.pago() != movimentacaoLocalizada.getPago()) {
                movimentacaoLocalizada.setPago(movimentacaoDTO.pago());
            }

            movimentacaoRepositorio.save(movimentacaoLocalizada);

        } catch (ErroGlobalMovimentacaoException e) {
            throw new ErroGlobalMovimentacaoException(
                    "Erro ao editar receita",
                    "Erro ao atualizar receita, verifique o erro: " + e.getMessage());
        }
    }

    // Funcionando
    public void editarDespesa(AlterarMovimentacaoDTO movimentacaoDTO,
            MultipartFile comprovanteEntrada,
            MultipartFile comprovanteRestante) {
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

            // Atualização de campos básicos
            if (movimentacaoDTO.descricao() != null && !movimentacaoDTO.descricao().isBlank()) {
                movimentacaoLocalizada.setDescricao(movimentacaoDTO.descricao());
            }
            if (movimentacaoDTO.data() != null) {
                movimentacaoLocalizada.setData(movimentacaoDTO.data());
            }
            if (movimentacaoDTO.valor() != null) {
                if (movimentacaoDTO.valor() != null) {
                    double valorAntigo = movimentacaoLocalizada.getValor();
                    double valorNovo = movimentacaoDTO.valor();
                    double diferenca = valorNovo - valorAntigo;

                    movimentacaoLocalizada.setValor(valorNovo);
                    Usuario usuarioLocalizado = movimentacaoLocalizada.getUsuario();
                    usuarioLocalizado.setSaldo(usuarioLocalizado.getSaldo() + diferenca);
                    usuarioRepositorio.save(usuarioLocalizado);
                }
            }
            if (movimentacaoDTO.tipo() != null) {
                movimentacaoLocalizada.setTipo(movimentacaoDTO.tipo());
            }
            if (movimentacaoDTO.tipoPagamento() != null) {
                movimentacaoLocalizada.setTipoPagamento(movimentacaoDTO.tipoPagamento());
            }
            if (movimentacaoDTO.lancamentoRecorrente() != null) {
                movimentacaoLocalizada.setLancamentoRecorrente(movimentacaoDTO.lancamentoRecorrente());
            }
            if (movimentacaoDTO.periodicidade() != null) {
                movimentacaoLocalizada.setPeriodicidade(movimentacaoDTO.periodicidade());
            }
            if (movimentacaoDTO.dataFimRecorrencia() != null) {
                movimentacaoLocalizada.setDataFimRecorrencia(movimentacaoDTO.dataFimRecorrencia());
            }
            if (movimentacaoDTO.totalRecorrencias() != null) {
                movimentacaoLocalizada.setTotalRecorrencias(movimentacaoDTO.totalRecorrencias());
            }

            // Atualizar categoria
            if (movimentacaoDTO.categoria_id() != null) {
                Categoria categoriaLocalizada = categoriaRepositorio.findById(movimentacaoDTO.categoria_id().getId())
                        .orElseThrow(() -> new CategoriaCadastradaException(
                                "Categoria não localizada",
                                "Favor validar se a categoria está cadastrada."));
                movimentacaoLocalizada.setCategoria_id(categoriaLocalizada);
            }

            // Atualizar comprovantes (se enviados)
            if (comprovanteEntrada != null && !comprovanteEntrada.isEmpty()) {
                String caminhoEntrada = salvarArquivo(comprovanteEntrada); // método auxiliar
                movimentacaoLocalizada.setComprovanteEntrada(caminhoEntrada);
            }
            if (comprovanteRestante != null && !comprovanteRestante.isEmpty()) {
                String caminhoRestante = salvarArquivo(comprovanteRestante);
                movimentacaoLocalizada.setComprovanteRestante(caminhoRestante);
            }

            if (movimentacaoDTO.pago() != movimentacaoLocalizada.getPago()) {
                movimentacaoLocalizada.setPago(movimentacaoDTO.pago());
                movimentacaoLocalizada.setDataDePagamento(LocalDateTime.now());
                movimentacaoLocalizada.setAtrasado(false);
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

    public Movimentacao listarMovimentacaoPorId(Long idMovimentacao) {
        return movimentacaoRepositorio.findById(idMovimentacao)
                .orElseThrow(() -> new MovimentacaoNaoLocalizadaException("Movimentação nao localizada",
                        "Movimentação nao localizada, favor criar uma."));
    }

    private String salvarArquivo(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String diretorio = "uploads/comprovantes/";

            String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();

            File pasta = new File(diretorio);
            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            Path path = Paths.get(diretorio + nomeArquivo);
            Files.write(path, file.getBytes());

            System.out.println("Salvando em: " + path.toAbsolutePath());
            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage(), e);
        }
    }

}
