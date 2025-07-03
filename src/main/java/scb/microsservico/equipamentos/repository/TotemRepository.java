package scb.microsservico.equipamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scb.microsservico.equipamentos.model.Totem;

public interface TotemRepository extends JpaRepository<Totem, Long> {
    
}