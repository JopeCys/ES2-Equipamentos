package scb.microsservico.equipamentos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class Bicicleta {
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;

    private Integer numero;
    private String marca;
    private String modelo;
    private String ano;
    private BicicletaStatus status;
}
