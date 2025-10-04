package Sistema.Financeiro.Fincaneiro.Servicos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import Sistema.Financeiro.Fincaneiro.DTO.DespesaCategoriaDTO;
import Sistema.Financeiro.Fincaneiro.DTO.ExtratoMovimentacaoDTO;
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

    // private Double calcularPorcentagemPorCategoria(Usuario usuarioLogado) {
    // LocalDate inicioMes = LocalDate.of(LocalDate.now().getYear(),
    // LocalDate.now().getMonthValue(), 1);
    // LocalDate fimMes = LocalDate.of(LocalDate.now().getYear(),
    // LocalDate.now().getMonthValue(), 31);

    // List<Movimentacao> movimentacoes = movimentacaoRepositorio.findAll()
    // .stream()
    // .filter(e -> e.isAtiva() && e.getTipo() == TipoMovimentacao.DESPESA &&
    // e.getData().isAfter(inicioMes)
    // && e.getData().isBefore(fimMes) && e.getUsuario().getId() ==
    // usuarioLogado.getId())
    // .toList();

    // Double valorTotalPorCategoria = movimentacoes.stream()
    // .mapToDouble(Movimentacao::getValor)
    // .sum();

    // return movimentacoes.stream()
    // .mapToDouble(e -> (e.getValor() / valorTotalPorCategoria) * 100)
    // .sum();
    // }

    public List<DespesaCategoriaDTO> despesasPorCategoria(Usuario usuarioLogado) {
        LocalDate inicioMes = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        List<Movimentacao> movimentacoes = movimentacaoRepositorio.findByUsuarioIdAndTipoAndDataBetweenAndAtiva(
                usuarioLogado.getId(),
                TipoMovimentacao.DESPESA,
                inicioMes,
                fimMes,
                true);

        Map<String, Double> totalPorCategoria = movimentacoes.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getCategoria_id().getNome(),
                        Collectors.summingDouble(Movimentacao::getValor)));

        double valorTotal = totalPorCategoria.values().stream().mapToDouble(Double::doubleValue).sum();

        List<DespesaCategoriaDTO> resultado = totalPorCategoria.entrySet().stream()
                .map(entry -> new DespesaCategoriaDTO(
                        entry.getKey(),
                        entry.getValue(),
                        (entry.getValue() / valorTotal) * 100))
                .collect(Collectors.toList());

        return resultado;
    }

    public List<ExtratoMovimentacaoDTO> extratoMovimentacao(Usuario usuarioLogado) {
        return movimentacaoRepositorio.findExtratoByUsuarioIdAndAtiva(usuarioLogado.getId(), true);
    }
}
