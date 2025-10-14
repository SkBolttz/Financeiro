package Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDuplicadoException extends RuntimeException {

    private String erro;
    private String message;

    public EmailDuplicadoException(String erro, String message) {
        super(message);
        this.erro = erro;
        this.message = message;
    }
    
}
