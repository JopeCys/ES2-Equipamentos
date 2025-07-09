package scb.microsservico.equipamentos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import scb.microsservico.equipamentos.enums.TrancaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class Tranca {
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;
    
    @NotNull
    private Integer numero;
    
    @NotNull
    private TrancaStatus status;
    
    @NotBlank
    private String anoDeFabricacao;
    
    @NotBlank
    private String modelo;
    
    private Integer bicicleta;

    private String localizacao;
}