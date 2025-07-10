package scb.microsservico.equipamentos.dto.Bicicleta;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class BicicletaCreateDTO {
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String ano;
}