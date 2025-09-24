package Sistema.Financeiro.Fincaneiro.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import Sistema.Financeiro.Fincaneiro.DTO.LoginDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RegistroDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Seguranca.TokenJWT;
import Sistema.Financeiro.Fincaneiro.Seguranca.TokenService;
import Sistema.Financeiro.Fincaneiro.Servicos.LoginServico;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LoginServico loginServico;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.email(), loginDTO.senha());

        Authentication authentication = authManager.authenticate(authenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenJWT(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody @Valid RegistroDTO registroDTO) {
        if (loginServico.cadastrar(new Usuario(registroDTO.nome(), registroDTO.email(), registroDTO.senha()))) {
            return ResponseEntity.status(201).body("Usuario cadastrado com sucesso!");
        } else {
            return ResponseEntity.status(409).body("Falha ao cadastrar usuario!");
        }
    }
}
