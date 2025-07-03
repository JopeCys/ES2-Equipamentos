package scb.microsservico.equipamentos.dto.Totem;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TotemResponseDTO {
    private Long id;
    private String localizacao;
    private String descricao;
}