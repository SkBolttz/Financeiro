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

    public AlertarController(MovimentacaoServico movimentacaoServico, UsuarioRepositorio usuarioRepositorio,
            UsuarioServico usuarioServico) {
        this.movimentacaoServico = movimentacaoServico;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioServico = usuarioServico;
    }

    private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @GetMapping("/verificar/vencimento/amanha")
    public ResponseEntity<List<AlertaDTO>> verificarVencimentoAgora(Principal principal) {
        Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(principal.getName());

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        List<Movimentacao> vencemAmanha = movimentacaoServico.verificarPertoVencimento(usuario.getId());

        List<AlertaDTO> alertas = vencemAmanha.stream()
                .map(m -> new AlertaDTO(
                        usuario.getNome(),
                        m.getDescricao(),
                        m.getData().toString()))
                .toList();

        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/alterar/vencimento/despesa")
    public ResponseEntity<List<AlertaDTO>> alterarVencimentoDespesa(Principal principal) {
        // Busca o usuário logado pelo email
        Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(principal.getName());

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Chama o serviço apenas para o usuário logado
        List<Movimentacao> vencidas = movimentacaoServico.alterarDespesaVencida(usuario.getId());

        // Converte para DTO
        List<AlertaDTO> alertas = vencidas.stream()
                .map(m -> new AlertaDTO(
                        usuario.getNome(),
                        m.getDescricao(),
                        m.getData().toString()))
                .toList();

        return ResponseEntity.ok(alertas);
    }
}
