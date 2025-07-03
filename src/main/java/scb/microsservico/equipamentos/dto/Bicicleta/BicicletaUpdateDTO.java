package scb.microsservico.equipamentos.dto.Bicicleta;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class BicicletaUpdateDTO {
    private String marca;
    private String modelo;
    private String ano;
}