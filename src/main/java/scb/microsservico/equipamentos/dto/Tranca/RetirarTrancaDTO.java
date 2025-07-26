package scb.microsservico.equipamentos.dto.Tranca;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import scb.microsservico.equipamentos.enums.AcaoRetirar;

@Data // Gera getters, setters e outros métodos automaticamente
public class RetirarTrancaDTO {
    @NotNull
    private Long idTranca;

    @NotNull
    private Long idTotem;

    @NotNull
    private Long idFuncionario;

    @NotNull
    private AcaoRetirar statusAcaoReparador;
}