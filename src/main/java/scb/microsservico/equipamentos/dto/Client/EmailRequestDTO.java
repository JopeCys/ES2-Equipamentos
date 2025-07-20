package scb.microsservico.equipamentos.dto.Client;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data // Gera getters, setters e outros métodos automaticamente
public class EmailRequestDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String assunto;

    @NotBlank
    private String mensagem;
}
