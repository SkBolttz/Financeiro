package Sistema.Financeiro.Fincaneiro.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.DTO.CadastroDTO;
import Sistema.Financeiro.Fincaneiro.DTO.LoginDTO;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao.CredenciaisInvalidasException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao.EmailDuplicadoException;
import Sistema.Financeiro.Fincaneiro.Seguranca.TokenJWT;
import Sistema.Financeiro.Fincaneiro.Servicos.LoginServico;
import Sistema.Financeiro.Fincaneiro.Swagger.RetornoSwagger.ErroResponse;
import Sistema.Financeiro.Fincaneiro.Swagger.RetornoSwagger.SucessoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// Configuração do Swagger para documentação da API
// Refatorado 
// Não precisa mais atualizar

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AutenticacaoController {

    @Autowired
    private LoginServico loginServico;

    @Operation(summary = "Login de usuário", description = "Autentica o usuário com email e senha, gerando um token JWT válido para acesso aos demais endpoints.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenJWT.class), examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponse.class), examples = @ExampleObject(value = "{ \"mensagem\": \"Credenciais inválidas\", \"status\": 401 }"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponse.class), examples = @ExampleObject(value = "{ \"mensagem\": \"Erro inesperado no servidor\", \"status\": 500 }")))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenJWT> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.status(200).body(loginServico.login(loginDTO).getBody());
        } catch (BadCredentialsException e) {
            throw new CredenciaisInvalidasException("Credenciais inválidas", "Usuário ou senha incorretos.");
        }
    }

    @Operation(summary = "Cadastro de usuário", description = "Cadastra um novo usuário no sistema com nome, email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Usuário cadastrado com sucesso!\""))),
            @ApiResponse(responseCode = "409", description = "Conflito - E-mail já cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponse.class), examples = @ExampleObject(value = "{ \"mensagem\": \"E-mail já cadastrado\", \"status\": 409 }"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponse.class), examples = @ExampleObject(value = "{ \"mensagem\": \"Erro inesperado no servidor\", \"status\": 500 }")))
    })
    @PostMapping("/cadastro")
    public ResponseEntity<SucessoResponse> cadastro(@Valid @RequestBody CadastroDTO cadastroDTO) {
        try {
            loginServico.cadastrar(cadastroDTO);
            return ResponseEntity.status(201)
                    .body(new SucessoResponse("Usuário cadastrado com sucesso!"));
        } catch (EmailDuplicadoException e) {
            throw new EmailDuplicadoException("E-mail já cadastrado",
                    "O e-mail informado já está em uso por outro usuário.");
        }
    }
}
