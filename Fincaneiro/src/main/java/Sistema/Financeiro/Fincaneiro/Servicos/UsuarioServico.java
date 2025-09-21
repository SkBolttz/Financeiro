package Sistema.Financeiro.Fincaneiro.Servicos;

import org.springframework.stereotype.Service;

import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;

@Service
public class UsuarioServico {
    
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }
    
}
