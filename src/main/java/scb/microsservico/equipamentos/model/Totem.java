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
    
    @OneToMany // Relacionamento um-para-muitos com Tranca
    @JoinColumn(name = "idTotem")
    private List<Tranca> trancas;
}