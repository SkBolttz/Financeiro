package Sistema.Financeiro.Fincaneiro.Schedule;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import Sistema.Financeiro.Fincaneiro.Servicos.MovimentacaoServico;

@Service
public class VencimentoSchedule {

    private MovimentacaoServico movimentacaoServico;
    private final UsuarioRepositorio usuarioRepositorio;

    public VencimentoSchedule(MovimentacaoServico movimentacaoServico, UsuarioRepositorio usuarioRepositorio) {
        this.movimentacaoServico = movimentacaoServico;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void verificarPertoVencimentoAgendado() {
        try {
            List<Usuario> usuarios = usuarioRepositorio.findAll();

            for (Usuario usuario : usuarios) {
                List<Movimentacao> vencemAmanha = movimentacaoServico.verificarPertoVencimento(usuario.getId());

                vencemAmanha.forEach(m -> {
                    System.out.println("Alerta para " + usuario.getNome() +
                            ": despesa " + m.getDescricao() +
                            " vence amanhã (" + m.getData() + ")");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void alterarDespesaVencida() {
        try {
            List<Usuario> usuarios = usuarioRepositorio.findAll();

            for (Usuario usuario : usuarios) {
                List<Movimentacao> alterarVencimento = movimentacaoServico.alterarDespesaVencida(usuario.getId());

                alterarVencimento.forEach(m -> {
                    System.out.println("Alerta para " + usuario.getNome() +
                            ": despesa " + m.getDescricao() +
                            " Vencida (" + m.getData() + ")");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
