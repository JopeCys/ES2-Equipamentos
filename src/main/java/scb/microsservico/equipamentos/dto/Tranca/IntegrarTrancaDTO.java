package scb.microsservico.equipamentos.dto.Tranca;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class IntegrarTrancaDTO {
    @NotNull
    private Long idTranca;
    @NotNull
    private Long idTotem;
    @NotNull
    private Long idFuncionario;
}