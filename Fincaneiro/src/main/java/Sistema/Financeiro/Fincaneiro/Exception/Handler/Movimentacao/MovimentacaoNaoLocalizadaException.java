package Sistema.Financeiro.Fincaneiro.Exception.Handler.Movimentacao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentacaoNaoLocalizadaException extends RuntimeException {

    private String message;
    private String detail;

    public MovimentacaoNaoLocalizadaException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
