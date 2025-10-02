package Sistema.Financeiro.Fincaneiro.Controlador;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Sistema.Financeiro.Fincaneiro.DTO.CategoriaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaCadastradaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.ErroGlobalCategoria;
import Sistema.Financeiro.Fincaneiro.Servicos.CategoriaServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias")
public class CategoriaController {

    private final CategoriaServico categoriaServico;

    public CategoriaController(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @Operation(summary = "Cadastrar nova categoria", description = "Cadastra uma nova categoria no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Categoria cadastrada com sucesso: NomeDaCategoria\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar a categoria (já existe ou dados inválidos)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao cadastrar a categoria: Categoria já existe\"")))
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarCategoria(@RequestBody @Valid Categoria categoria) {
        try {
            categoriaServico.cadastrarCategoria(categoria);
            return ResponseEntity.status(201)
                    .body("Categoria cadastrada com sucesso: " + categoria.getNome());
        } catch (CategoriaCadastradaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @Operation(summary = "Remover categoria", description = "Remove uma categoria existente pelo ID ou nome.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria removida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Categoria removida com sucesso\""))),
            @ApiResponse(responseCode = "400", description = "Categoria não encontrada ou erro ao remover", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao localizar a categoria, verifique se está cadastrada\"")))
    })
    @PutMapping("/remover")
    public ResponseEntity<String> removerCategoria(@RequestBody @Valid CategoriaDTO categoria) {
        try {
            categoriaServico.removerCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria removida com sucesso");
        } catch (CategoriaNaoLocalizadaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @Operation(summary = "Editar categoria", description = "Atualiza os dados de uma categoria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria editada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Categoria editada com sucesso\""))),
            @ApiResponse(responseCode = "400", description = "Categoria não encontrada ou erro ao editar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao localizar a categoria, verifique se está cadastrada\"")))
    })
    @PutMapping("/editar")
    public ResponseEntity<String> editarCategoria(@RequestBody @Valid CategoriaDTO categoria) {
        try {
            categoriaServico.editarCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria editada com sucesso");
        } catch (CategoriaNaoLocalizadaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Não utilizado
    @Operation(summary = "Listar todas as categorias", description = "Retorna todas as categorias cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Nenhuma categoria encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Nenhuma categoria encontrada\"")))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        try {
            return ResponseEntity.ok(categoriaServico.listarCategorias());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @Operation(summary = "Listar categorias ativas", description = "Retorna apenas as categorias ativas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias ativas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Nenhuma categoria ativa encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Nenhuma categoria ativa encontrada\"")))
    })
    @GetMapping("/listar/ativas")
    public ResponseEntity<List<Categoria>> listarCategoriasAtivas() {
        try {
            return ResponseEntity.ok(categoriaServico.listarCategoriasAtivas());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @Operation(summary = "Listar categorias inativas", description = "Retorna apenas as categorias inativas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias inativas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Nenhuma categoria inativa encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Nenhuma categoria inativa encontrada\"")))
    })
    @GetMapping("/listar/inativas")
    public ResponseEntity<List<Categoria>> listarCategoriasInativas() {
        try {
            return ResponseEntity.ok(categoriaServico.listarCategoriasInativas());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @Operation(summary = "Listar categorias de receita", description = "Retorna apenas as categorias classificadas como receita.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias de receita retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Nenhuma categoria de receita encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Nenhuma categoria de receita encontrada\"")))
    })
    @GetMapping("/listar/receita/ativas")
    public ResponseEntity<List<Categoria>> listarCategoriasReceita() {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasReceita();
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @Operation(summary = "Listar categorias de despesa", description = "Retorna apenas as categorias classificadas como despesa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias de despesa retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Nenhuma categoria de despesa encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Nenhuma categoria de despesa encontrada\"")))
    })
    @GetMapping("/listar/despesa/ativas")
    public ResponseEntity<List<Categoria>> listarCategoriasDespesa() {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasDespesa();
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
