package scb.microsservico.equipamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import scb.microsservico.equipamentos.model.Bicicleta;

import java.util.List;
import java.util.Optional;

// Interface para operações de banco de dados da entidade Bicicleta
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    boolean existsByNumero(int numero);
    Optional<Bicicleta> findByNumero(int numero);
    List<Bicicleta> findAllByNumeroIn(List<Integer> numeros);
}
