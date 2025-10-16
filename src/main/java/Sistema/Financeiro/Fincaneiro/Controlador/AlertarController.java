package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.DTO.AlertaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import Sistema.Financeiro.Fincaneiro.Servicos.MovimentacaoServico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;

@RestController
@RequestMapping("/alerta")
public class AlertarController {

    private final MovimentacaoServico movimentacaoServico;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioServico usuarioServico;

    public AlertarController(MovimentacaoServico movimentacaoServico, UsuarioRepositorio usuarioRepositorio, UsuarioServico usuarioServico) {
        this.movimentacaoServico = movimentacaoServico;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioServico = usuarioServico;
    }

        private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @GetMapping("/verificar/vencimento/amanha")
    public ResponseEntity<List<AlertaDTO>> verificarVencimentoAgora(Principal principal) {
        List<Usuario> usuarios = usuarioRepositorio.findByNome(principal.getName());
        List<AlertaDTO> alertas = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            List<Movimentacao> vencemAmanha = movimentacaoServico.verificarPertoVencimento(usuario.getId());

            vencemAmanha.forEach(m -> {
                alertas.add(new AlertaDTO(
                        usuario.getNome(),
                        m.getDescricao(),
                        m.getData().toString()));
            });
        }

        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/alterar/vencimento/despesa")
    public ResponseEntity<List<AlertaDTO>> alterarVencimentoDespesa() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        List<AlertaDTO> alertas = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            List<Movimentacao> alterarVencimento = movimentacaoServico.alterarDespesaVencida(usuario.getId());

            alterarVencimento.forEach(m -> {
                alertas.add(new AlertaDTO(
                        usuario.getNome(),
                        m.getDescricao(),
                        m.getData().toString()));
            });
        }
        return ResponseEntity.ok(alertas);
    }
}
