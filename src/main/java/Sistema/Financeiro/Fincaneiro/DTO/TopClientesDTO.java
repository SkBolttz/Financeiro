package Sistema.Financeiro.Fincaneiro.DTO;

public record TopClientesDTO(
        Long clienteId,
        String clienteNome,
        long totalMovimentacoes
) {}
