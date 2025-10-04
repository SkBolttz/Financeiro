package Sistema.Financeiro.Fincaneiro.Servicos;

import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Limite;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.LimiteRepositorio;

@Service
public class LimiteServico {

    private final LimiteRepositorio limiteRepositorio;

    public LimiteServico(LimiteRepositorio limiteRepositorio) {
        this.limiteRepositorio = limiteRepositorio;
    }

    public Limite cadastrarLimiteAtual(Limite limite) {
        Limite limiteExistente = limiteRepositorio.findByUsuarioId(limite.getUsuario().getId());

        if (limiteExistente != null) {
            limiteExistente.setLimite(limite.getLimite());
            limiteExistente.setAtivo(true);
            return limiteRepositorio.save(limiteExistente);
        } else {
            limite.setAtivo(true);
            return limiteRepositorio.save(limite);
        }
    }

    public Limite listarLimite(Usuario usuarioLogado) {
        return limiteRepositorio.findByUsuarioId(usuarioLogado.getId());
    }
}
