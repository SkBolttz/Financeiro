package Sistema.Financeiro.Fincaneiro.Servicos;

import org.springframework.stereotype.Service;

import Sistema.Financeiro.Fincaneiro.DTO.EmailDTO;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;

@Service
public class UsuarioServico {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario resgatarUsuario(EmailDTO emailDTO) {
        Usuario usuario = (Usuario) usuarioRepositorio.findByEmail(emailDTO.email());
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado para o email: " + emailDTO.email());
        }
        return usuario;
    }
}
