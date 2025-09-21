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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaServico categoriaServico;

    public CategoriaController(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarCategoria(@RequestBody @Valid Categoria categoria) {
        try {
            categoriaServico.cadastrarCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria cadastrada com sucesso: " + categoria.getNome());
        } catch (CategoriaCadastradaException e) {
            return ResponseEntity.status(400).body(
                    "Erro ao cadastrar a categoria, verifique se a categoria ja foi cadastrada." + e.getMessage());
        } catch (ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(
                    "Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    @PutMapping("/remover")
    public ResponseEntity<String> removerCategoria(@RequestBody @Valid CategoriaDTO categoria) {
        try {
            categoriaServico.removerCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria removida com sucesso:");
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body("Erro ao localizar a categoria, por favor, verique se esta cadastrada");
        } catch (ErroGlobalCategoria e) {
            return ResponseEntity.status(400)
                    .body("Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<String> editarCategoria(@RequestBody @Valid CategoriaDTO categoria) {
        try {
            categoriaServico.editarCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria editada com sucesso:");
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body("Erro ao localizar a categoria, por favor, verique se esta cadastrada");
        } catch (ErroGlobalCategoria e) {
            return ResponseEntity.status(400)
                    .body("Erro ao cadastrar a categoria, por valide, valide o erro: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        try {
            return ResponseEntity.status(200).body(categoriaServico.listarCategorias());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body(null);
        }
    }

    @GetMapping("/listar/ativas")
    public ResponseEntity<List<Categoria>> listarCategoriasAtivas() {
        try {
            return ResponseEntity.status(200).body(categoriaServico.listarCategoriasAtivas());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body(null);
        }
    }

    @GetMapping("listar/inativas")
    public ResponseEntity<List<Categoria>> listarCategoriasInativas() {
        try {
            return ResponseEntity.status(200).body(categoriaServico.listarCategoriasInativas());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body(null);
        }
    }

    @GetMapping("/listar/receita")
    public ResponseEntity<List<Categoria>> listarCategoriasReceita() {
        try {
            return ResponseEntity.status(200).body(categoriaServico.listarCategoriasReceita());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body(null);
        }
    }

    @GetMapping("/listar/despesa")
    public ResponseEntity<List<Categoria>> listarCategoriasDespesa() {
        try {
            return ResponseEntity.status(200).body(categoriaServico.listarCategoriasDespesa());
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400)
                    .body(null);
        }
    }
}
