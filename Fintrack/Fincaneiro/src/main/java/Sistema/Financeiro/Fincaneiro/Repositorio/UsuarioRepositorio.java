package Sistema.Financeiro.Fincaneiro.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    boolean existsByEmailAndSenha(String email, String senha);

    boolean existsByEmail(String email);

    Usuario findByEmailAndSenha(String email, String senha);

    UserDetails findByEmail(String username);


}
