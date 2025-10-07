package Sistema.Financeiro.Fincaneiro.Exception.Handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao.CredenciaisInvalidasException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaCadastradaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaIncorretaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.CategoriaNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria.ErroGlobalCategoria;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Cliente.ClienteNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorCadastradoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Fornecedor.FornecedorNaoLocalizadoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.ErroGlobalMovimentacaoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.ListarMovimentacaoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.MovimentacaoInativaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.MovimentacaoNaoLocalizadaException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao.TipoIncorretoException;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Servidor.ErroServidorExcption;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Usuario.UsuarioNaoLocalizadoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErroGlobalCategoria.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaException(ErroGlobalCategoria ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Categoria");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CategoriaNaoLocalizadaException.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaException(CategoriaNaoLocalizadaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Categoria");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CategoriaCadastradaException.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaException(CategoriaCadastradaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Categoria");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CategoriaIncorretaException.class)
    public ResponseEntity<Map<String, Object>> handleCategoriaException(CategoriaIncorretaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Categoria");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ErroGlobalMovimentacaoException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(ErroGlobalMovimentacaoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MovimentacaoNaoLocalizadaException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(MovimentacaoNaoLocalizadaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(TipoIncorretoException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(TipoIncorretoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(UsuarioNaoLocalizadoException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(UsuarioNaoLocalizadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MovimentacaoInativaException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(MovimentacaoInativaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ListarMovimentacaoException.class)
    public ResponseEntity<Map<String, Object>> handleMovimentacaoException(ListarMovimentacaoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Movimentação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(CredenciaisInvalidasException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(ErroServidorExcption.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(ErroServidorExcption ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(FornecedorCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(FornecedorCadastradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(FornecedorNaoLocalizadoException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(
            FornecedorNaoLocalizadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ClienteCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(ClienteCadastradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ClienteNaoLocalizadoException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidasException(ClienteNaoLocalizadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Autenticação");
        body.put("details", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
