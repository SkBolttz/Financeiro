package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.*;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoClienteFornecedor;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Repositorio.ClienteRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.FornecedorRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;

@Service
public class DashboardSevico {

    private final MovimentacaoRepositorio movimentacaoRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final FornecedorRepositorio fornecedorRepositorio;

    public DashboardSevico(
            MovimentacaoRepositorio movimentacaoRepositorio,
            ClienteRepositorio clienteRepositorio,
            FornecedorRepositorio fornecedorRepositorio) {
        this.movimentacaoRepositorio = movimentacaoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.fornecedorRepositorio = fornecedorRepositorio;
    }

    // ================== Gráficos existentes ==================

    public List<Movimentacao> tendenciaGastosDespesasAnual(Usuario usuarioLogado) {
        LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fimAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.DESPESA,
                inicioAno,
                fimAno,
                true);
    }

    public List<Movimentacao> tendenciaGastosReceitasAnual(Usuario usuarioLogado) {
        LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fimAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.RECEITA,
                inicioAno,
                fimAno,
                true);
    }

    public List<DespesaCategoriaDTO> despesasPorCategoria(Usuario usuarioLogado) {
        LocalDate inicioMes = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        List<Movimentacao> movimentacoes = movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.DESPESA,
                inicioMes,
                fimMes,
                true);

        Map<String, Double> totalPorCategoria = movimentacoes.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getCategoria_id().getNome(),
                        Collectors.summingDouble(Movimentacao::getValor)));

        double valorTotal = totalPorCategoria.values().stream().mapToDouble(Double::doubleValue).sum();

        return totalPorCategoria.entrySet().stream()
                .map(entry -> new DespesaCategoriaDTO(
                        entry.getKey(),
                        entry.getValue(),
                        (entry.getValue() / valorTotal) * 100))
                .collect(Collectors.toList());
    }

    public List<ExtratoMovimentacaoDTO> extratoMovimentacao(Usuario usuarioLogado) {
        return movimentacaoRepositorio.findExtratoByUsuarioIdAndAtiva(usuarioLogado.getId(), true);
    }

    // ================== Novos gráficos ==================

    public ClientesTipoDTO clientesPorTipo(Usuario usuario) {
        long pf = clienteRepositorio.countByUsuarioAndTipo(usuario, TipoClienteFornecedor.PF);
        long pj = clienteRepositorio.countByUsuarioAndTipo(usuario, TipoClienteFornecedor.PJ);
        return new ClientesTipoDTO(pf, pj);
    }

    public List<ClientesPorMesDTO> clientesPorMes(Usuario usuario) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusMonths(12);
        return clienteRepositorio.findClientesPorMes(usuario.getId(), inicio, hoje);
    }

    public List<TopClientesDTO> topClientes(Usuario usuario) {
        return movimentacaoRepositorio.findTopClientesPorMovimentacoes(usuario.getId());
    }

    public List<TopFornecedoresDTO> topFornecedoresPorMovimentacoes(Usuario usuario) {
        return movimentacaoRepositorio.findTopFornecedoresPorDespesas(usuario.getId());
    }

    public TotalReceitaDespesaDTO totalReceitasXDespesas(Usuario usuario) {
        Double receitas = movimentacaoRepositorio.calcularSomaPorTipo(TipoMovimentacao.RECEITA, usuario.getId());
        Double despesas = movimentacaoRepositorio.calcularSomaPorTipo(TipoMovimentacao.DESPESA, usuario.getId());
        return new TotalReceitaDespesaDTO(
                receitas != null ? receitas : 0,
                despesas != null ? despesas : 0);
    }

    public List<MovimentacaoCategoriaDTO> receitasDespesasPorCategoria(Usuario usuario) {
        return movimentacaoRepositorio.findTotaisPorCategoria(usuario.getId());
    }

    public List<MovimentacaoMensalDTO> receitasDespesasPorMes(Usuario usuario) {
        return movimentacaoRepositorio.findTotaisPorMes(usuario.getId());
    }

    public List<MovimentacaoRecorrenteDTO> receitasDespesasRecorrentes(Usuario usuario) {
        return movimentacaoRepositorio.findRecorrentesPorUsuario(usuario.getId())
                .stream()
                .map(m -> new MovimentacaoRecorrenteDTO(
                        m.getId(),
                        m.getDescricao(),
                        m.getValor(),
                        m.getDataLancamentoRecorrenteCriacao(),
                        m.getDataLancamentoRecorrenteProxima(),
                        calcularPeriodicidadeDias(m),
                        m.getTotalRecorrencias(),
                        m.getRecorrenciasCriadas()))
                .collect(Collectors.toList());
    }

    public List<MovimentacaoTipoPagamentoDTO> receitasDespesasPorTipoPagamento(Usuario usuario) {
        return movimentacaoRepositorio.findTotaisPorTipoPagamento(usuario.getId());
    }

    // ================== KPIs ==================

    public Long totalClientesAtivos(Usuario usuarioLogado) {
        return clienteRepositorio.countByUsuarioAndAtivoTrue(usuarioLogado);
    }

    public Long totalFornecedores(Usuario usuarioLogado) {
        return fornecedorRepositorio.countByUsuario(usuarioLogado);
    }

    public Long totalFornecedoresAtivos(Usuario usuarioLogado) {
        return fornecedorRepositorio.countByUsuarioAndAtivoTrue(usuarioLogado);
    }

    public FinanceiroKpiDTO totalReceitasDespesasESaldo(Usuario usuarioLogado) {
        Double receitas = movimentacaoRepositorio.calcularSomaPorTipo(TipoMovimentacao.RECEITA, usuarioLogado.getId());
        Double despesas = movimentacaoRepositorio.calcularSomaPorTipo(TipoMovimentacao.DESPESA, usuarioLogado.getId());
        double saldo = (receitas != null ? receitas : 0) - (despesas != null ? despesas : 0);
        return new FinanceiroKpiDTO(receitas != null ? receitas : 0, despesas != null ? despesas : 0, saldo);
    }

    // ================== Helper ==================

    private int calcularPeriodicidadeDias(Movimentacao m) {
        if (m.getPeriodicidade() == null)
            return 0;
        return switch (m.getPeriodicidade()) {
            case DIARIO -> 1;
            case SEMANAL -> 7;
            case MENSAL -> 30;
            case ANUAL -> 365;
            default -> 0;
        };
    }
}
