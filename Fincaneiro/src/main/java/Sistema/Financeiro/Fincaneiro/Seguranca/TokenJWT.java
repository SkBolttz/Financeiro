package Sistema.Financeiro.Fincaneiro.Seguranca;

import org.springframework.http.ResponseEntity;

public record TokenJWT(String token) {

    public TokenJWT(ResponseEntity<TokenJWT> login) {
        this(login.getBody().token());
    }
}
