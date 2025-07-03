package scb.microsservico.equipamentos.dto.Tranca;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TrancaCreateDTO {
    private Integer numero;
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
}