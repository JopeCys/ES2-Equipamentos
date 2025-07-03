package scb.microsservico.equipamentos.repository;

import scb.microsservico.equipamentos.model.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface para operações de banco de dados da entidade Bicicleta
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    
}