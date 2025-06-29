package scb.microsservico.equipamentos.dto;

import lombok.Data;
import scb.microsservico.equipamentos.enums.TrancaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaResponseDTO {
    private Long id;
    private Long bicicleta;
    private Integer numero;
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
    private TrancaStatus status;
}