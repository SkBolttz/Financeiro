package Sistema.Financeiro.Fincaneiro.Controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Sistema.Financeiro.Fincaneiro.DTO.AlterarMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.MovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.DTO.RemoverMovimentacaoDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaIncorretaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.TipoIncorretoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Usuario.UsuarioNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Servicos.MovimentacaoServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    private final MovimentacaoServico movimentacaoServico;

    public MovimentacaoController(MovimentacaoServico movimentacaoServico) {
        this.movimentacaoServico = movimentacaoServico;
    }

    @PostMapping("/adicionar/receita")
    public ResponseEntity<String> adicionarReceita(@RequestBody @Valid MovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.adicionarReceita(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Receita adicionada com sucesso: " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (UsuarioNaoLocalizadoException e) {
            return ResponseEntity.status(400).body(
                    "Usuário nao localizado, favor criar um.");
        } catch (TipoIncorretoException e) {
            return ResponseEntity.status(400).body(
                    "Tipo de movimentação inválido, esperado RECEITA.");
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body("Erro ao localizar a categoria, por favor, verique se esta cadastrada");
        } catch (CategoriaIncorretaException e) {
            return ResponseEntity.status(400).body("Categoria nao e uma categoria de receita.");
        }
    }

    @PutMapping("/remover/receita")
    public ResponseEntity<String> removerReceita(@RequestBody @Valid RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.removerReceita(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Receita removida com sucesso: ");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao remover receita: " + e.getMessage());
        }
    }

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

    @PostMapping("/adicionar/despesa")
    public ResponseEntity<String> adicionarDespesa(@RequestBody @Valid MovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.adicionarDespesa(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Despesa adicionada com sucesso: " + movimentacaoDTO.descricao() + " - "
                            + movimentacaoDTO.valor() + " - " + movimentacaoDTO.data() + " - "
                            + movimentacaoDTO.tipo());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao adicionar despesa: " + e.getMessage());
        }
    }

    @PutMapping("/remover/despesa")
    public ResponseEntity<String> removerDespesa(@RequestBody @Valid RemoverMovimentacaoDTO movimentacaoDTO) {
        try {
            movimentacaoServico.removerDespesa(movimentacaoDTO);
            return ResponseEntity.status(201)
                    .body("Despesa removida com sucesso:");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao remover despesa: " + e.getMessage());
        }
    }

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

    @GetMapping("/listar/todas")
    public ResponseEntity<List<Movimentacao>> listarMovimentacao() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarMovimentacao());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/receita")
    public ResponseEntity<List<Movimentacao>> listarReceitas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarReceitas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa")
    public ResponseEntity<List<Movimentacao>> listarDespesas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarDespesas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/receita/ativa")
    public ResponseEntity<List<Movimentacao>> listarReceitasAtivas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarReceitasAtivas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/receita/inativa")
    public ResponseEntity<List<Movimentacao>> listarReceitasInativas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarReceitasInativas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa/ativa")
    public ResponseEntity<List<Movimentacao>> listarDespesasAtivas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarDespesasAtivas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa/inativa")
    public ResponseEntity<List<Movimentacao>> listarDespesasInativas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarDespesasInativas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa/paga")
    public ResponseEntity<List<Movimentacao>> listarDespesasPagas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarDespesasPagas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa/atrasada")
    public ResponseEntity<List<Movimentacao>> listarDespesasAtrasadas() {
        try {
            return ResponseEntity.status(200).body(movimentacaoServico.listarDespesasAtrasadas());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
