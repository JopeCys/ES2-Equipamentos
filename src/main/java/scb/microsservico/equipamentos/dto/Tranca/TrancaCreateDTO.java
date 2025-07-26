package scb.microsservico.equipamentos.dto.Tranca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import scb.microsservico.equipamentos.enums.TrancaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaCreateDTO {
    @NotNull
    private Integer numero;
    @NotBlank
    private String localizacao;
    @NotBlank
    private String anoDeFabricacao;
    
    // Não obrigatórios, estão aqui para mera adequação ao swagger
    private String modelo;
    private TrancaStatus status;
}