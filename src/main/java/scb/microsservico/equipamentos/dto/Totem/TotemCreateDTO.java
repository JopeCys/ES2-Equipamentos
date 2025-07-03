package scb.microsservico.equipamentos.dto.Totem;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TotemCreateDTO {
    private String localizacao;
    private String descricao;
}