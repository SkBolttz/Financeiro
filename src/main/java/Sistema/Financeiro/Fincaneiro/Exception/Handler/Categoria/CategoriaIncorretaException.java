package Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaIncorretaException extends RuntimeException {

    private String message;
    private String detail;

    public CategoriaIncorretaException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
