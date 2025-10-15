package Sistema.Financeiro.Fincaneiro.Schedule;

import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;
import Sistema.Financeiro.Fincaneiro.Servicos.MovimentacaoServico;

@Service
public class LancamentoRecorrenteSchedule {

    private final MovimentacaoRepositorio movimentacaoRepositorio;

    public LancamentoRecorrenteSchedule(MovimentacaoServico movimentacaoServico,
            MovimentacaoRepositorio movimentacaoRepositorio) {
        this.movimentacaoRepositorio = movimentacaoRepositorio;
    }

    @Scheduled(cron = "0 0 * * * ?") 
    public void processarLancamentosRecorrentes() {
        List<Movimentacao> recorrentes = movimentacaoRepositorio.findByLancamentoRecorrenteTrueAndAtivaTrue();
        recorrentes.forEach(this::gerarNovaRecorrenciaSeNecessario);
    }

    public void gerarNovaRecorrenciaSeNecessario(Movimentacao m) {
        LocalDate hoje = LocalDate.now();

        if (m.getDataLancamentoRecorrenteProxima() != null && !hoje.isBefore(m.getDataLancamentoRecorrenteProxima())) {

            if (m.getDataFimRecorrencia() != null && hoje.isAfter(m.getDataFimRecorrencia())) {
                m.setAtiva(false);
                movimentacaoRepositorio.save(m);
                return;
            }

            if (m.getTotalRecorrencias() != null &&
                    m.getRecorrenciasCriadas() != null &&
                    m.getRecorrenciasCriadas() >= m.getTotalRecorrencias()) {
                m.setAtiva(false);
                movimentacaoRepositorio.save(m);
                return;
            }

            if (!m.getPago() && hoje.isAfter(m.getDataLancamentoRecorrenteProxima())) {
                m.setAtrasado(true);
            }

            Movimentacao nova = new Movimentacao();
            nova.setDescricao(m.getDescricao());
            nova.setValor(m.getValor());
            nova.setTipo(m.getTipo());
            nova.setUsuario(m.getUsuario());
            nova.setCategoria_id(m.getCategoria_id());
            nova.setTipoPagamento(m.getTipoPagamento());

            nova.setData(m.getDataLancamentoRecorrenteProxima());
            nova.setPago(false);
            nova.setAtiva(true);
            nova.setAtrasado(false);

            nova.setLancamentoRecorrente(true);
            nova.setPeriodicidade(m.getPeriodicidade());
            nova.setDataFimRecorrencia(m.getDataFimRecorrencia());
            nova.setTotalRecorrencias(m.getTotalRecorrencias());
            nova.setRecorrenciasCriadas(m.getRecorrenciasCriadas() == null ? 1 : m.getRecorrenciasCriadas() + 1);

            nova.setDataLancamentoRecorrenteCriacao(nova.getData());
            nova.setDataLancamentoRecorrenteProxima(calcularProximaRecorrencia(nova));

            movimentacaoRepositorio.save(nova);
        }
    }

    private LocalDate calcularProximaRecorrencia(Movimentacao m) {
        return switch (m.getPeriodicidade()) {
            case DIARIO -> m.getData().plusDays(1);
            case SEMANAL -> m.getData().plusWeeks(1);
            case MENSAL -> m.getData().plusMonths(1);
            case ANUAL -> m.getData().plusYears(1);
        };
    }
}
