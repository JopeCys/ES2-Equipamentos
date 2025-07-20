package scb.microsservico.equipamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class RegistroOperacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String tipo; // "INTEGRACAO" ou "RETIRADA"

    @NotBlank
    private String descricao;

    @NotBlank
    private LocalDateTime dataHora;

    @NotBlank
    private Long idFuncionario;
}