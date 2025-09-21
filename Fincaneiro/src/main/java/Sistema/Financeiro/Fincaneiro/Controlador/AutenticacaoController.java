package Sistema.Financeiro.Fincaneiro.Controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import Sistema.Financeiro.Fincaneiro.DTO.LoginDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RegistroDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Seguranca.JwtUtil;
import Sistema.Financeiro.Fincaneiro.Servicos.LoginServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final LoginServico loginServico;
    private final JwtUtil jwtUtil;

    public AutenticacaoController(LoginServico loginServico, JwtUtil jwtUtil) {
        this.loginServico = loginServico;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        if (loginServico.autenticar(loginDTO.email(), loginDTO.senha())) {
            String token = jwtUtil.generateToken(loginDTO.email());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody @Valid RegistroDTO registroDTO) {
        if (loginServico.cadastrar(new Usuario(registroDTO.nome(), registroDTO.email(), registroDTO.senha()))) {
            return ResponseEntity.status(201).body("Usuario cadastrado com sucesso!");
        } else {
            return ResponseEntity.status(409).body("Falha ao cadastrar usuario!");
        }
    }

    //Endpoint para recuperar senha
    
}
