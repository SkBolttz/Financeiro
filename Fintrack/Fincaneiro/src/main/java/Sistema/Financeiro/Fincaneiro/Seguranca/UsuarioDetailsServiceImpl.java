    package Sistema.Financeiro.Fincaneiro.Seguranca;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;


    @Service
    public class UsuarioDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UsuarioRepositorio repository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return repository.findByEmail(username);
        }
    }