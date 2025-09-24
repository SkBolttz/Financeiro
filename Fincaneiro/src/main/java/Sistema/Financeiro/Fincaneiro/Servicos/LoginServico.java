package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;

@Service
public class LoginServico {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public LoginServico(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // Funcionando
    public boolean autenticar(String email, String senha) {
        Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(email);
        if (usuario == null)
            return false;

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.matches(senha, usuario.getSenha());
    }

    // Funcionando
    public boolean cadastrar(Usuario usuario) {

        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            return false;
        }

        usuario.setSaldo(0.0);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setDataCadatro(LocalDate.now());

        usuarioRepositorio.save(usuario);
        return true;
    }
}
