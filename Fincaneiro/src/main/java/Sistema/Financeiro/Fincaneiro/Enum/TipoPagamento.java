package Sistema.Financeiro.Fincaneiro.Enum;

import lombok.Getter;

@Getter
public enum TipoPagamento {
    PIX("Pix"),
    BOLETO("Boleto"),
    DINHEIRO("Dinheiro"),
    CARTAO_CREDITO("Cartao Credito"),
    CARTAO_DEBITO("Cartao Debito"),
    TRANSFERENCIA("Transferencia"),
    DEPOSITO("Deposito"),
    CHEQUE("Cheque"),
    OUTRO("Outro");

    private String descricao;

    TipoPagamento(String descricao) {
        this.descricao = descricao;
    }
}
