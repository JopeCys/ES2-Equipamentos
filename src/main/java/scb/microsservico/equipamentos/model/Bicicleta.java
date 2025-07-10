package scb.microsservico.equipamentos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import scb.microsservico.equipamentos.enums.BicicletaStatus;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class Bicicleta {
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;
    
    @NotNull
    private Integer numero; // Gerado automaticamente de forma aleatória, não é necessário informar ao criar a bicicleta
    
    @NotNull
    private BicicletaStatus status; // Sempre que uma bicicleta é criada, ela inicia com o status "NOVA"
    
    @NotBlank
    private String marca;
    
    @NotBlank
    private String modelo;
    
    @NotBlank
    private String ano;
    
    private String localizacao; // Localização da bicicleta, é a localização da tranca onde ela está trancada
}
