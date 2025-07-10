package scb.microsservico.equipamentos.dto.Tranca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaCreateDTO {

    @NotNull
    private Integer numero;

    @NotBlank
    private String anoDeFabricacao;

    @NotBlank
    private String modelo;
}