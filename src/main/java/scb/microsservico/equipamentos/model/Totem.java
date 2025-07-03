package scb.microsservico.equipamentos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data // Gera getters, setters e outros métodos automaticamente
@Entity // Marca como entidade JPA
public class Totem {
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String localizacao;
    private String descricao;

    @OneToMany
    @JoinColumn(name = "totem_id")
    private List<Tranca> trancas;
}