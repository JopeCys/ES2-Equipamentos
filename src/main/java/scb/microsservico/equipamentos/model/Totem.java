package scb.microsservico.equipamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import java.util.List;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class Totem {
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String localizacao;

    @NotBlank
    private String descricao;
    
    // Relacionamento uma-para-muitos com a entidade Tranca
    // mappedBy indica que a relação é gerenciada pelo campo 'totem' na classe Tranca.
    @OneToMany(mappedBy = "totem")
    private List<Tranca> trancas = new java.util.ArrayList<>();
}