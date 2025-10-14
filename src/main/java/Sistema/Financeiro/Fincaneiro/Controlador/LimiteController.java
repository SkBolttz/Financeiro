package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.Entidade.Limite;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Servicos.LimiteServico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/limite")
public class LimiteController {

    private final LimiteServico limiteServico;
    private final UsuarioServico usuarioServico;

    public LimiteController(LimiteServico limiteServico, UsuarioServico usuarioServico) {
        this.limiteServico = limiteServico;
        this.usuarioServico = usuarioServico;
    }

    private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Limite> cadastrarOuAtualizarLimite(@RequestBody @Valid Limite limite) {
        try {
            Limite limiteAtual = limiteServico.cadastrarLimiteAtual(limite);
            return ResponseEntity.status(HttpStatus.CREATED).body(limiteAtual);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/listar/limite/cliente")
    public ResponseEntity<Limite> listarLimite(Principal principal) {
        try {
            Limite limiteAtual = limiteServico.listarLimite(getUsuarioLogado(principal));
            return ResponseEntity.ok(limiteAtual);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/verificar/limite")
    public ResponseEntity<Limite> verificarLimite(Principal principal) {
        try {
            Limite limiteAtual = limiteServico.verificarLimite(getUsuarioLogado(principal));
            return ResponseEntity.ok(limiteAtual);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
