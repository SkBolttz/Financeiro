package Sistema.Financeiro.Fincaneiro.DTO;

import java.time.LocalDate;

public record MovimentacaoRecorrenteDTO(
        Long movimentacaoId,
        String descricao,
        double valor,
        LocalDate dataCriacao,
        LocalDate proximaData,
        int periodicidadeDias,
        int totalRecorrencias,
        int recorrenciasCriadas
) {}