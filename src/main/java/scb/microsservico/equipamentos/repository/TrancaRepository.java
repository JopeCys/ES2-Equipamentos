// repository/TrancaRepository.java
package scb.microsservico.equipamentos.repository;

import scb.microsservico.equipamentos.model.Tranca;

import org.springframework.data.jpa.repository.JpaRepository;

// Interface para operações de banco de dados da entidade Tranca
public interface TrancaRepository extends JpaRepository<Tranca, Long> {

}