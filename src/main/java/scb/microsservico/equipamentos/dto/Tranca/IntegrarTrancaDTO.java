package scb.microsservico.equipamentos.dto.Tranca;

import lombok.Data;

@Data
public class IntegrarTrancaDTO {
     // @NotNull
    private Long idTranca;

    // @NotNull
    private Long idTotem;

    // @NotNull
    private Long idFuncionario;
}
