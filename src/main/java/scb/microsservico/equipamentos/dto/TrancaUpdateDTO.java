package scb.microsservico.equipamentos.dto;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaUpdateDTO {
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
}