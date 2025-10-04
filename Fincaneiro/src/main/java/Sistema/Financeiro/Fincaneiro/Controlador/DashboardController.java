package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/resumo")
    public ResponseEntity<?> resumo(Principal principal) {
        try {
            return ResponseEntity.ok(dashboardSevico.resumo(getUsuarioLogado(principal)));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
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

}
