package scb.microsservico.equipamentos.dto.Tranca;

import lombok.Data;

@Data
public class RetirarTrancaDTO {
    // @NotNull
    private Long idTranca;

    // @NotNull
    private Long idTotem;

    // @NotNull
    private Long idFuncionario;
}
