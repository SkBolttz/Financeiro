package Sistema.Financeiro.Fincaneiro.Enum;

import lombok.Getter;

@Getter
public enum TipoMovimentacao {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }
}
