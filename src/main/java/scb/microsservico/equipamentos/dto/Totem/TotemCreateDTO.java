package scb.microsservico.equipamentos.dto.Totem;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class TotemCreateDTO {
    @NotBlank
    private String localizacao;
    @NotBlank
    private String descricao;
}