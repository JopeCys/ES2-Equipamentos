package scb.microsservico.equipamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import scb.microsservico.equipamentos.model.Totem;

// Interface para operações de banco de dados da entidade Bicicleta
public interface TotemRepository extends JpaRepository<Totem, Long> {
    
}