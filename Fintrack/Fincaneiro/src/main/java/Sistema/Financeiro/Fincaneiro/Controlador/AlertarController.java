package Sistema.Financeiro.Fincaneiro.Controlador;

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

@RestController
@RequestMapping("/alerta")
public class AlertarController {

    private final MovimentacaoServico movimentacaoServico;
    private final UsuarioRepositorio usuarioRepositorio;

    public AlertarController(MovimentacaoServico movimentacaoServico, UsuarioRepositorio usuarioRepositorio) {
        this.movimentacaoServico = movimentacaoServico;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/verificar/vencimento/amanha")
    public ResponseEntity<List<AlertaDTO>> verificarVencimentoAgora() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
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
