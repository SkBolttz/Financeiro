package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.DTO.ClientesPorMesDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ClientesTipoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.DespesaCategoriaDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ExtratoMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.FinanceiroKpiDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoCategoriaDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoMensalDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoRecorrenteDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoTipoPagamentoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.TopClientesDTO;
import Sistema.Financeiro.Fincaneiro.DTO.TopFornecedoresDTO;
import Sistema.Financeiro.Fincaneiro.DTO.TotalReceitaDespesaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Servicos.DashboardSevico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardSevico dashboardSevico;
    private final UsuarioServico usuarioServico;

    public DashboardController(DashboardSevico dashboardSevico, UsuarioServico usuarioServico) {
        this.dashboardSevico = dashboardSevico;
        this.usuarioServico = usuarioServico;
    }

    private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @GetMapping("/tendencia/gastos/despesas/anual")
    public ResponseEntity<List<Movimentacao>> tendenciaGastosDespesasAnual(Principal principal) {
        try {
            return ResponseEntity.ok(dashboardSevico.tendenciaGastosDespesasAnual(getUsuarioLogado(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/tendencia/gastos/receita/anual")
    public ResponseEntity<List<Movimentacao>> tendenciaGastosReceitasAnual(Principal principal) {
        try {
            return ResponseEntity.ok(dashboardSevico.tendenciaGastosReceitasAnual(getUsuarioLogado(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/despesas/por/categoria")
    public ResponseEntity<List<DespesaCategoriaDTO>> despesasPorCategoria(Principal principal) {
        try {
            return ResponseEntity.ok(dashboardSevico.despesasPorCategoria(getUsuarioLogado(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/extrato/movimentacao")
    public ResponseEntity<List<ExtratoMovimentacaoDTO>> extratoMovimentacao(Principal principal) {
        try {
            return ResponseEntity.ok(dashboardSevico.extratoMovimentacao(getUsuarioLogado(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
        // Clientes PF x PJ
    @GetMapping("/clientes/tipo")
    public ResponseEntity<ClientesTipoDTO> clientesPorTipo(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.clientesPorTipo(getUsuarioLogado(principal)));
    }

    // Clientes cadastrados por mês
    @GetMapping("/clientes/mensal")
    public ResponseEntity<List<ClientesPorMesDTO>> clientesPorMes(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.clientesPorMes(getUsuarioLogado(principal)));
    }

    // Top clientes por movimentações
    @GetMapping("/clientes/top-movimentacoes")
    public ResponseEntity<List<TopClientesDTO>> topClientes(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.topClientes(getUsuarioLogado(principal)));
    }

    // Top fornecedores por movimentações/despesas
    @GetMapping("/fornecedores/top-movimentacoes")
    public ResponseEntity<List<TopFornecedoresDTO>> topFornecedoresPorMovimentacoes(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.topFornecedoresPorMovimentacoes(getUsuarioLogado(principal)));
    }

    // Número de fornecedores cadastrados
    @GetMapping("/fornecedores/total")
    public ResponseEntity<Long> totalFornecedores(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.totalFornecedores(getUsuarioLogado(principal)));
    }

    // Total de receitas x despesas
    @GetMapping("/movimentacoes/total")
    public ResponseEntity<TotalReceitaDespesaDTO> totalReceitasXDespesas(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.totalReceitasXDespesas(getUsuarioLogado(principal)));
    }

    // Receitas e despesas por categoria
    @GetMapping("/movimentacoes/categoria")
    public ResponseEntity<List<MovimentacaoCategoriaDTO>> receitasDespesasPorCategoria(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.receitasDespesasPorCategoria(getUsuarioLogado(principal)));
    }

    // Receitas e despesas por mês
    @GetMapping("/movimentacoes/mensal")
    public ResponseEntity<List<MovimentacaoMensalDTO>> receitasDespesasPorMes(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.receitasDespesasPorMes(getUsuarioLogado(principal)));
    }

    // Receitas e despesas recorrentes
    @GetMapping("/movimentacoes/recorrentes")
    public ResponseEntity<List<MovimentacaoRecorrenteDTO>> receitasDespesasRecorrentes(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.receitasDespesasRecorrentes(getUsuarioLogado(principal)));
    }

    // Receitas x despesas por tipo de pagamento
    @GetMapping("/movimentacoes/tipo-pagamento")
    public ResponseEntity<List<MovimentacaoTipoPagamentoDTO>> receitasDespesasPorTipoPagamento(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.receitasDespesasPorTipoPagamento(getUsuarioLogado(principal)));
    }

    // =================== KPIs ===================

    @GetMapping("/kpi/clientes-ativos")
    public ResponseEntity<Long> totalClientesAtivos(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.totalClientesAtivos(getUsuarioLogado(principal)));
    }

    @GetMapping("/kpi/fornecedores-ativos")
    public ResponseEntity<Long> totalFornecedoresAtivos(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.totalFornecedoresAtivos(getUsuarioLogado(principal)));
    }

    @GetMapping("/kpi/financeiro")
    public ResponseEntity<FinanceiroKpiDTO> totalReceitasDespesasESaldo(Principal principal) {
        return ResponseEntity.ok(dashboardSevico.totalReceitasDespesasESaldo(getUsuarioLogado(principal)));
    }
}
