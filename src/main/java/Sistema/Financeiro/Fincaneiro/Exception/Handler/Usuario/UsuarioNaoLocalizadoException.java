package Sistema.Financeiro.Fincaneiro.Exception.Handler.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioNaoLocalizadoException extends RuntimeException {

    private String message;
    private String detail;

    public UsuarioNaoLocalizadoException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
