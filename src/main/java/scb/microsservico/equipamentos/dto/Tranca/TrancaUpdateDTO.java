package scb.microsservico.equipamentos.dto.Tranca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import scb.microsservico.equipamentos.enums.TrancaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaUpdateDTO {
    @NotNull
    private Integer numero;
    @NotBlank
    private String anoDeFabricacao;
    @NotBlank
    private String modelo;
    
    // Não obrigatórios, estão aqui para mera adequação ao swagger
    private String localizacao;
    private TrancaStatus status;
}