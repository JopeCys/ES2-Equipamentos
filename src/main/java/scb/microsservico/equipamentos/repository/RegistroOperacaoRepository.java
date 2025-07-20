package scb.microsservico.equipamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scb.microsservico.equipamentos.model.RegistroOperacao;

public interface RegistroOperacaoRepository extends JpaRepository<RegistroOperacao, Long> {}