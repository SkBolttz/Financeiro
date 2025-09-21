package Sistema.Financeiro.Fincaneiro.Exception.Handler.Categoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroGlobalCategoria extends RuntimeException {

    private String message;
    private String detail;

    public ErroGlobalCategoria(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
