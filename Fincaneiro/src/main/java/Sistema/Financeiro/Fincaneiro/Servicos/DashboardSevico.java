package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import Sistema.Financeiro.Fincaneiro.Entidade.Movimentacao;
import Sistema.Financeiro.Fincaneiro.Entidade.Usuario;
import Sistema.Financeiro.Fincaneiro.Enum.TipoMovimentacao;
import Sistema.Financeiro.Fincaneiro.Repositorio.MovimentacaoRepositorio;

@Service
public class DashboardSevico {

    private final MovimentacaoRepositorio movimentacaoRepositorio;

    public DashboardSevico(MovimentacaoRepositorio movimentacaoRepositorio) {
        this.movimentacaoRepositorio = movimentacaoRepositorio;
    }

    public Object resumo(Usuario usuarioLogado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resumo'");
    }

    public List<Movimentacao> tendenciaGastosDespesasAnual(Usuario usuarioLogado) {
        LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fimAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.DESPESA,
                inicioAno,
                fimAno,
                true);
    }

    public List<Movimentacao> tendenciaGastosReceitasAnual(Usuario usuarioLogado) {
        LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fimAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        return movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.RECEITA,
                inicioAno,
                fimAno,
                true);
    }
}
