package Sistema.Financeiro.Fincaneiro.Exception.Handler.Servidor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroServidorExcption extends RuntimeException {

    private String erro;
    private String message;

    public ErroServidorExcption(String erro, String message) {
        super(message);
        this.erro = erro;
        this.message = message;
    }

}
