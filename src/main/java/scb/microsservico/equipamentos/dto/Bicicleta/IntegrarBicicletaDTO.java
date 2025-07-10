package scb.microsservico.equipamentos.dto.Bicicleta;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class IntegrarBicicletaDTO {
    @NotNull
    private Long idTranca;

    @NotNull
    private Long idBicicleta;

    @NotNull
    private Long idFuncionario;
}