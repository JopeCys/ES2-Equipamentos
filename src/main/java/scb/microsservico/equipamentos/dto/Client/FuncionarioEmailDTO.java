package scb.microsservico.equipamentos.dto.Client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data // Gera getters, setters e outros métodos automaticamente
public class FuncionarioEmailDTO {
    @NotBlank
    private String email;
}
