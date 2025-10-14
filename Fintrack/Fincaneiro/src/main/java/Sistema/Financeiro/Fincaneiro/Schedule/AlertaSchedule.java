package Sistema.Financeiro.Fincaneiro.Schedule;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Limite;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Repositorio.UsuarioRepositorio;
import Sistema.Financeiro.Fincaneiro.Servicos.LimiteServico;

@Service
public class AlertaSchedule {

    private final LimiteServico limiteServico;
    private final UsuarioRepositorio usuarioRepositorio;

    public AlertaSchedule(LimiteServico limiteServico, UsuarioRepositorio usuarioRepositorio) {
        this.limiteServico = limiteServico;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void verificarLimite() {
        try {
            List<Usuario> usuarios = usuarioRepositorio.findAll();

            for (Usuario usuario : usuarios) {
                Limite limite = limiteServico.verificarLimite(usuario);

                if (limite != null) {
                    System.out.println(
                            "⚠️ Usuário " + usuario.getNome() + " ultrapassou o limite: " + limite.getLimite());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
