package Sistema.Financeiro.Fincaneiro.Seguranca;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // NÃO tente lidar com OPTIONS aqui; CORS já trata isso
        String tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            String email = tokenService.validarToken(tokenJWT);
            Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(email);
            if (usuario != null) {
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
