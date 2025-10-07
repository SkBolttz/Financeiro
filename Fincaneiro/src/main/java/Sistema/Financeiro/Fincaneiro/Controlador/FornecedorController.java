package Sistema.Financeiro.Fincaneiro.Controlador;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarFornecedorDTO;
import Sistema.Financeiro.Fincaneiro.DTO.FornecedorDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Fornecedor;
import Sistema.Financeiro.Fincaneiro.Servicos.FornecedorServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedor", description = "Endpoints para gerenciamento de fornecedores")
@Validated
public class FornecedorController {

    private final FornecedorServico fornecedorServico;

    public FornecedorController(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    @PostMapping
    @Operation(summary = "Adicionar fornecedor", description = "Cria um novo fornecedor com os dados fornecidos.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Fornecedor> adicionarFornecedor(@RequestBody @Validated FornecedorDTO dto) {
        Fornecedor fornecedorCriado = fornecedorServico.adicionarFornecedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorCriado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar fornecedor", description = "Atualiza os dados de um fornecedor existente pelo ID.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Fornecedor> alterarFornecedor(
            @PathVariable @Parameter(description = "ID do fornecedor a ser alterado") Long id,
            @RequestBody @Validated AlterarFornecedorDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(fornecedorServico.alterarFornecedor(dto));
    }

    @Operation(summary = "Desativar fornecedor", description = "Marca o fornecedor como inativo.")
    @PutMapping("/desativar/{id}")
    public ResponseEntity<String> desativarFornecedor(@PathVariable Long id) {
        fornecedorServico.desativarFornecedor(id);
        return ResponseEntity.ok("Fornecedor desativado com sucesso");
    }

    @Operation(summary = "Ativar fornecedor", description = "Marca o fornecedor como ativo novamente.")
    @PutMapping("/ativar/{id}")
    public ResponseEntity<String> ativarFornecedor(@PathVariable Long id) {
        fornecedorServico.ativarFornecedor(id);
        return ResponseEntity.ok("Fornecedor ativado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar fornecedores", description = "Retorna uma lista de todos os fornecedores cadastrados.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<Fornecedor>> listarFornecedores() {
        return ResponseEntity.ok(fornecedorServico.listarFornecedores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna os dados de um fornecedor específico pelo ID.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Fornecedor> buscarPorId(
            @PathVariable @Parameter(description = "ID do fornecedor a ser buscado") Long id) {
        return ResponseEntity.ok(fornecedorServico.buscarPorId(id));
    }
}
