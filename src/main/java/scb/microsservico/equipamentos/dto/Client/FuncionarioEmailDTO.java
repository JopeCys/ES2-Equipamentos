package scb.microsservico.equipamentos.dto.Client;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class FuncionarioEmailDTO {
    @NotBlank
    private String email;
}
