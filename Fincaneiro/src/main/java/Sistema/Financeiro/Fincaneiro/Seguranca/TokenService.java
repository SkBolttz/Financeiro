package Sistema.Financeiro.Fincaneiro.Seguranca;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private long expirationMillis;

    public String gerarToken(Usuario usuario) {
        return JWT.create()
                .withIssuer("FinTrack")
                .withSubject(usuario.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validarToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("FinTrack")
                .build()
                .verify(token)
                .getSubject();
    }
}