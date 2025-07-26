package scb.microsservico.equipamentos.dto.Bicicleta;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import scb.microsservico.equipamentos.enums.AcaoRetirar;

@Data // Gera getters, setters e outros métodos automaticamente
public class RetirarBicicletaDTO {
    @NotNull
    private Long idTranca;

    @NotNull
    private Long idBicicleta;

    @NotNull
    private Long idFuncionario;

    @NotNull
    private AcaoRetirar statusAcaoReparador;
}