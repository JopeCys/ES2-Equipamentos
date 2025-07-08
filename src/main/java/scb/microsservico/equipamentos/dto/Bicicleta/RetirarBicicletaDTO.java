package scb.microsservico.equipamentos.dto.Bicicleta;

import lombok.Data;

@Data
public class RetirarBicicletaDTO {
    private Long idTranca;

    private Long idBicicleta;

    private Long idFuncionario;
}
