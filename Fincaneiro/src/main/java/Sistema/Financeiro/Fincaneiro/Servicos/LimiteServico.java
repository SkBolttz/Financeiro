package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Limite;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Repositorio.LimiteRepositorio;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;

@Service
public class LimiteServico {

    private final LimiteRepositorio limiteRepositorio;
    private final MovimentacaoRepositorio movimentacaoRepositorio;

    public LimiteServico(LimiteRepositorio limiteRepositorio, MovimentacaoRepositorio movimentacaoRepositorio) {
        this.limiteRepositorio = limiteRepositorio;
        this.movimentacaoRepositorio = movimentacaoRepositorio;
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

    public Limite verificarLimite(Usuario usuarioLogado) {
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate inicioMes = hoje.withDayOfMonth(1);
            LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

            Limite limite = limiteRepositorio.findByUsuarioId(usuarioLogado.getId());

            if (limite == null || !limite.getAtivo()) {
                return null;
            }

            List<Movimentacao> movimentacoes = movimentacaoRepositorio
                    .findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                            usuarioLogado.getId(),
                            TipoMovimentacao.DESPESA,
                            inicioMes,
                            fimMes,
                            true);

            double valorTotal = movimentacoes.stream()
                    .mapToDouble(Movimentacao::getValor)
                    .sum();

            return valorTotal > limite.getLimite() ? limite : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
