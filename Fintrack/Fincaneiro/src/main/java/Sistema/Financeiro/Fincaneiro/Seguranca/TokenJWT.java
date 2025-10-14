package Sistema.Financeiro.Fincaneiro.Seguranca;

import org.springframework.http.ResponseEntity;

public record TokenJWT(String token) {

    @SuppressWarnings("null")
    public TokenJWT(ResponseEntity<TokenJWT> login) {
        this(login.getBody().token());
    }
}
