package Sistema.Financeiro.Fincaneiro.Enum;

import lombok.Getter;

@Getter
public enum TipoClienteFornecedor {
    PF("Pessoa Fisica"),
    PJ("Pessoa Juridica");

    private final String descricao;

    TipoClienteFornecedor(String descricao) {
        this.descricao = descricao;
    }
}
