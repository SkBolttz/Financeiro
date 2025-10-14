package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarFornecedorDTO;
import Sistema.Financeiro.Fincaneiro.DTO.FornecedorDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Servicos.FornecedorServico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedor", description = "Endpoints para gerenciamento de fornecedores")
@Validated
public class FornecedorController {

    private final FornecedorServico fornecedorServico;
    private final UsuarioServico usuarioServico;

    public FornecedorController(FornecedorServico fornecedorServico, UsuarioServico usuarioServico) {
        this.fornecedorServico = fornecedorServico;
        this.usuarioServico = usuarioServico;
    }

    private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @PostMapping("/adicionar")
    @Operation(summary = "Adicionar fornecedor", description = "Cria um novo fornecedor com os dados fornecidos.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Fornecedor> adicionarFornecedor(@RequestBody @Validated FornecedorDTO dto, Principal principal) {
        Fornecedor fornecedorCriado = fornecedorServico.adicionarFornecedor(dto, getUsuarioLogado(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorCriado);
    }

    @PutMapping("/editar/{id}")
    @Operation(summary = "Alterar fornecedor", description = "Atualiza os dados de um fornecedor existente pelo ID.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Fornecedor> alterarFornecedor(
            @PathVariable @Parameter(description = "ID do fornecedor a ser alterado") Long id,
            @RequestBody @Validated AlterarFornecedorDTO dto, Principal principal) {
        dto.setId(id);
        return ResponseEntity.ok(fornecedorServico.alterarFornecedor(dto, getUsuarioLogado(principal)));
    }

    @Operation(summary = "Desativar fornecedor", description = "Marca o fornecedor como inativo.")
    @PutMapping("/desativar/{id}")
    public ResponseEntity<String> desativarFornecedor(@PathVariable Long id, Principal principal) {
        fornecedorServico.desativarFornecedor(id, getUsuarioLogado(principal));
        return ResponseEntity.ok("Fornecedor desativado com sucesso");
    }

    @Operation(summary = "Ativar fornecedor", description = "Marca o fornecedor como ativo novamente.")
    @PutMapping("/ativar/{id}")
    public ResponseEntity<String> ativarFornecedor(@PathVariable Long id, Principal principal) {
        fornecedorServico.ativarFornecedor(id, getUsuarioLogado(principal));
        return ResponseEntity.ok("Fornecedor ativado com sucesso");
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar fornecedores", description = "Retorna uma lista de todos os fornecedores cadastrados.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<Fornecedor>> listarFornecedores(Principal principal) {
        return ResponseEntity
                .ok(fornecedorServico.listarFornecedores(getUsuarioLogado(principal)));
    }

    @GetMapping("/listar/ativos")
    @Operation(summary = "Listar fornecedores", description = "Retorna uma lista de todos os fornecedores ativos cadastrados.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<Fornecedor>> listarFornecedoresAtivos(Principal principal) {
        List<Fornecedor> fornecedores = fornecedorServico.listarFornecedoresAtivos(getUsuarioLogado(principal));
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna os dados de um fornecedor específico pelo ID.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Fornecedor> buscarPorId(
            @PathVariable @Parameter(description = "ID do fornecedor a ser buscado") Long id, Principal principal) {
        return ResponseEntity
                .ok(fornecedorServico.buscarPorIdEUsuario(id, getUsuarioLogado(principal)));
    }
}
