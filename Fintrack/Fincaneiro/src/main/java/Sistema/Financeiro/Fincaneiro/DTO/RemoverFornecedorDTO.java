package Sistema.Financeiro.Fincaneiro.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RemoverFornecedorDTO {
    @NotNull
    private Long id;
}
