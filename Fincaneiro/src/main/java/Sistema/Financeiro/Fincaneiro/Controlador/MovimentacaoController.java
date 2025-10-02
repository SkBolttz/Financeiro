package Sistema.Financeiro.Fincaneiro.Controlador;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import Sistema.Financeiro.Fincaneiro.DTO.AlterarMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RemoverMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaIncorretaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.TipoIncorretoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Usuario.UsuarioNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Servicos.MovimentacaoServico;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimentacao")
@Tag(name = "Movimentação", description = "Endpoints para gerenciamento de movimentações (receitas e despesas)")
public class MovimentacaoController {

    private final MovimentacaoServico movimentacaoServico;

    public MovimentacaoController(MovimentacaoServico movimentacaoServico) {
        this.movimentacaoServico = movimentacaoServico;
    }

    @Operation(summary = "Adicionar receita", description = "Adiciona uma nova receita ao usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Receita adicionada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Receita adicionada com sucesso: Descrição - Valor - Data - Tipo\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao adicionar receita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao adicionar receita: Usuário não localizado / Tipo incorreto / Categoria inválida\"")))
    })
    @PostMapping("/adicionar/receita")
    public ResponseEntity<String> adicionarReceita(@RequestBody @Valid MovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.adicionarReceita(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Receita adicionada com sucesso:V2 " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (UsuarioNaoLocalizadoException e) {
            return ResponseEntity.status(400).body("Usuário não localizado, favor criar um.");
        } catch (TipoIncorretoException e) {
            return ResponseEntity.status(400).body("Tipo de movimentação inválido, esperado RECEITA.");
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body("Erro ao localizar a categoria, por favor, verifique se está cadastrada");
        } catch (CategoriaIncorretaException e) {
            return ResponseEntity.status(400).body("Categoria não é uma categoria de receita.");
        }
    }

    @Operation(summary = "Remover receita", description = "Remove uma receita existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Receita removida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Receita removida com sucesso\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao remover receita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao remover receita: Mensagem de erro\"")))
    })
    @PutMapping("/remover/receita")
    public ResponseEntity<String> removerReceita(@RequestBody @Valid RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.removerReceita(movimentacaoDTO);
            return ResponseEntity.status(201).body("Receita removida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao remover receita: " + e.getMessage());
        }
    }

    @Operation(summary = "Editar receita", description = "Edita uma receita existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Receita editada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Receita editada com sucesso: Descrição - Valor - Data - Tipo\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao editar receita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao editar receita: Mensagem de erro\"")))
    })
    @PutMapping("/editar/receita")
    public ResponseEntity<String> editarReceita(@RequestBody @Valid AlterarMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.editarReceita(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Receita editada com sucesso: " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao editar receita: " + e.getMessage());
        }
    }

    @Operation(summary = "Adicionar despesa", description = "Adiciona uma nova despesa ao usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa adicionada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Despesa adicionada com sucesso: Descrição - Valor - Data - Tipo\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao adicionar despesa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao adicionar despesa: Mensagem de erro\"")))
    })
    @PostMapping("/adicionar/despesa")
    public ResponseEntity<String> adicionarDespesa(@RequestBody @Valid MovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.adicionarDespesa(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Despesa adicionada com sucesso:V2 " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao adicionar despesa: " + e.getMessage());
        }
    }

    @Operation(summary = "Remover despesa", description = "Remove uma despesa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa removida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Despesa removida com sucesso\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao remover despesa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao remover despesa: Mensagem de erro\"")))
    })
    @PutMapping("/remover/despesa")
    public ResponseEntity<String> removerDespesa(@RequestBody @Valid RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.removerDespesa(movimentacaoDTO);
            return ResponseEntity.status(201).body("Despesa removida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao remover despesa: " + e.getMessage());
        }
    }

    @Operation(summary = "Editar despesa", description = "Edita uma despesa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa editada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Despesa editada com sucesso: Descrição - Valor - Data - Tipo\""))),
            @ApiResponse(responseCode = "400", description = "Erro ao editar despesa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao editar despesa: Mensagem de erro\"")))
    })
    @PutMapping("/editar/despesa")
    public ResponseEntity<String> editarDespesa(@RequestBody @Valid AlterarMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.editarDespesa(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Despesa editada com sucesso: " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao editar despesa: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todas as movimentações", description = "Retorna todas as movimentações cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimentações retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimentacao.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar movimentações", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Erro ao listar movimentações: Mensagem de erro\"")))
    })
    @GetMapping("/listar/todas")
    public ResponseEntity<Page<Movimentacao>> listarMovimentacao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarMovimentacao(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/receita")
    public ResponseEntity<Page<Movimentacao>> listarReceita(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarReceitas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/despesa")
    public ResponseEntity<Page<Movimentacao>> listarDespesa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarDespesas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/receitas/ativas")
    public ResponseEntity<Page<Movimentacao>> listarReceitasAtivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarReceitasAtivas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/receitas/inativas")
    public ResponseEntity<Page<Movimentacao>> listarReceitasInativas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarReceitasInativas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/despesas/ativas")
    public ResponseEntity<Page<Movimentacao>> listarDespesasAtivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarDespesasAtivas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/despesas/inativas")
    public ResponseEntity<Page<Movimentacao>> listarDespesasInativas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarDespesasInativas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/despesas/pagas")
    public ResponseEntity<Page<Movimentacao>> listarDespesasPagas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarDespesasPagas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/despesas/atrasadas")
    public ResponseEntity<Page<Movimentacao>> listarDespesasAtrasadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarDespesasAtrasadas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/movimentacoes/ativas")
    public ResponseEntity<Page<Movimentacao>> listarMovimentacoesAtivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarMovimentacoesAtivas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar/movimentacoes/inativas")
    public ResponseEntity<Page<Movimentacao>> listarMovimentacoesInativas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<Movimentacao> movimentacoes = movimentacaoServico.listarMovimentacoesInativas(page, size);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
