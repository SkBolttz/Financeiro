package Sistema.Financeiro.Fincaneiro.Controlador;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Sistema.Financeiro.Fincaneiro.DTO.CategoriaDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Categoria;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaCadastradaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.ErroGlobalCategoria;
import Sistema.Financeiro.Fincaneiro.Servicos.CategoriaServico;
import Sistema.Financeiro.Fincaneiro.Servicos.UsuarioServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias")
public class CategoriaController {

    private final CategoriaServico categoriaServico;
    private final UsuarioServico usuarioServico;

    public CategoriaController(CategoriaServico categoriaServico, UsuarioServico usuarioServico) {
        this.categoriaServico = categoriaServico;
        this.usuarioServico = usuarioServico;
    }

    private Usuario getUsuarioLogado(Principal principal) {
        return usuarioServico.buscarPorEmail(principal.getName());
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar nova categoria", description = "Cadastra uma nova categoria no sistema.")
    public ResponseEntity<String> cadastrarCategoria(@RequestBody @Valid Categoria categoria, Principal principal) {
        try {
            categoria.setUsuario(getUsuarioLogado(principal));
            categoriaServico.cadastrarCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria cadastrada com sucesso: " + categoria.getNome());
        } catch (CategoriaCadastradaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/remover")
    @Operation(summary = "Remover categoria", description = "Remove uma categoria existente pelo ID ou nome.")
    public ResponseEntity<String> removerCategoria(@RequestBody @Valid CategoriaDTO categoria, Principal principal) {
        try {
            categoria.setUsuario(getUsuarioLogado(principal));
            categoriaServico.removerCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria removida com sucesso");
        } catch (CategoriaNaoLocalizadaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/editar")
    @Operation(summary = "Editar categoria", description = "Atualiza os dados de uma categoria existente.")
    public ResponseEntity<String> editarCategoria(@RequestBody @Valid CategoriaDTO categoria, Principal principal) {
        try {
            categoria.setUsuario(getUsuarioLogado(principal));
            categoriaServico.editarCategoria(categoria);
            return ResponseEntity.status(201).body("Categoria editada com sucesso");
        } catch (CategoriaNaoLocalizadaException | ErroGlobalCategoria e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas as categorias", description = "Retorna todas as categorias cadastradas pelo usuário.")
    public ResponseEntity<List<Categoria>> listarCategorias(Principal principal) {
        try {
            List<Categoria> categorias = categoriaServico.listarCategorias(getUsuarioLogado(principal));
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/ativas")
    @Operation(summary = "Listar categorias ativas", description = "Retorna apenas as categorias ativas do usuário.")
    public ResponseEntity<List<Categoria>> listarCategoriasAtivas(Principal principal) {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasAtivas(getUsuarioLogado(principal));
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/inativas")
    @Operation(summary = "Listar categorias inativas", description = "Retorna apenas as categorias inativas do usuário.")
    public ResponseEntity<List<Categoria>> listarCategoriasInativas(Principal principal) {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasInativas(getUsuarioLogado(principal));
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/receita/ativas")
    @Operation(summary = "Listar categorias de receita", description = "Retorna apenas as categorias de receita ativas do usuário.")
    public ResponseEntity<List<Categoria>> listarCategoriasReceita(Principal principal) {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasReceita(getUsuarioLogado(principal));
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/listar/despesa/ativas")
    @Operation(summary = "Listar categorias de despesa", description = "Retorna apenas as categorias de despesa ativas do usuário.")
    public ResponseEntity<List<Categoria>> listarCategoriasDespesa(Principal principal) {
        try {
            List<Categoria> categorias = categoriaServico.listarCategoriasDespesa(getUsuarioLogado(principal));
            return ResponseEntity.ok(categorias);
        } catch (CategoriaNaoLocalizadaException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
