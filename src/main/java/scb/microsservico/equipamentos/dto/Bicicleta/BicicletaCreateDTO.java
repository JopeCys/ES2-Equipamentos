package scb.microsservico.equipamentos.dto.Bicicleta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
public class BicicletaCreateDTO {
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String ano;

    // Não obrigatórios, estão aqui para mera adequação ao swagger
    private Integer numero;
    private BicicletaStatus status;
}