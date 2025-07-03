package scb.microsservico.equipamentos.dto.Bicicleta;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class BicicletaCreateDTO {
    private Integer numero;
    private String marca;
    private String modelo;
    private String ano;
    private String localizacao;
}