package scb.microsservico.equipamentos.dto.Bicicleta;

import lombok.Data;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
public class BicicletaResponseDTO {
    private Long id;
    private Integer numero;
    private String marca;
    private String modelo;
    private String ano;
    private BicicletaStatus status;
    private String localizacao;
}