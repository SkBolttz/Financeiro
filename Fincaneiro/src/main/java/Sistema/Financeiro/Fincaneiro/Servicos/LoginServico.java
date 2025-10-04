package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.DTO.CadastroDTO;
import Sistema.Financeiro.Fincaneiro.DTO.LoginDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Exception.Handler.Autenticacao.CredenciaisInvalidasException;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import Sistema.Financeiro.Fincaneiro.Seguranca.TokenJWT;
import Sistema.Financeiro.Fincaneiro.Seguranca.TokenService;

@Service
public class LoginServico {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    public LoginServico(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // Funcionando
    public boolean autenticar(LoginDTO loginDTO) {
        Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(loginDTO.email());
        if (usuario == null)
            return false;

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.matches(loginDTO.senha(), usuario.getSenha());
    }

    // Funcionando
    public boolean cadastrar(CadastroDTO cadastroDTO) {

        if (usuarioRepositorio.existsByEmail(cadastroDTO.email())) {
            return false;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(cadastroDTO.email());
        usuario.setNome(cadastroDTO.nome());
        usuario.setSenha(passwordEncoder.encode(cadastroDTO.senha()));
        usuario.setSaldo(0.0);
        usuario.setDataCadastro(LocalDate.now());

        usuarioRepositorio.save(usuario);
        return true;
    }

    public ResponseEntity<TokenJWT> login(LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.email(), loginDTO.senha());

            Authentication authentication = authManager.authenticate(authenticationToken);
            Usuario usuario = (Usuario) authentication.getPrincipal();
            String token = tokenService.gerarToken(usuario);

            return ResponseEntity.ok(new TokenJWT(token));
        } catch (BadCredentialsException e) {
            throw new CredenciaisInvalidasException("Credenciais inválidas", "Usuário ou senha incorretos.");
        }
    }
}
