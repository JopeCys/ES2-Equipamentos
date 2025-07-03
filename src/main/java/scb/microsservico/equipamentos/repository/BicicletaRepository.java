package scb.microsservico.equipamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scb.microsservico.equipamentos.model.Bicicleta;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    boolean existsByNumero(int numero);
}