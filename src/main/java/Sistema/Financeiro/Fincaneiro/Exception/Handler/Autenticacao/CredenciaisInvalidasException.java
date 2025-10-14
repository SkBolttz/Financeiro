package Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredenciaisInvalidasException extends RuntimeException {

    private String erro;
    private String message;

    public CredenciaisInvalidasException(String erro, String message) {
        super(message);
        this.erro = erro;
        this.message = message;
    }

}
