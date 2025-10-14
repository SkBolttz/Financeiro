package Sistema.Financeiro.Fincaneiro.DTO;

public record TopFornecedoresDTO(
        Long fornecedorId,
        String fornecedorNome,
        long totalMovimentacoes
) {}