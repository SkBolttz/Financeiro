package Sistema.Financeiro.Fincaneiro.Controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.DTO.EmailDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Endpoints relacionados a usuários")
public class UsuarioController {

    private final UsuarioServico usuarioServico;

    public UsuarioController(UsuarioServico usuarioServico) {
        this.usuarioServico = usuarioServico;
    }

    @Operation(summary = "Resgatar usuário por email", description = "Retorna os dados do usuário a partir do email fornecido.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário localizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(value = "{ \"id\": 1, \"nome\": \"Henrique\", \"email\": \"henrique@email.com\" }")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não localizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class),
                examples = @ExampleObject(value = "\"Usuário não localizado para o email informado.\"")
            )
        )
    })
    @PostMapping("/resgatar/usuario")
    public ResponseEntity<?> resgatarUsuario(@RequestBody EmailDTO emailDTO) {
        try {
            Usuario usuarioLocalizado = usuarioServico.resgatarUsuario(emailDTO);
            return ResponseEntity.ok(usuarioLocalizado);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Usuário não localizado para o email informado.");
        }
    }
}
