package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrancaRepositoryTest {

    @Autowired
    private TrancaRepository trancaRepository;

    @Test
    @DisplayName("Deve salvar e buscar uma tranca pelo ID")
    void testSaveAndFindById() {
        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setLocalizacao("A");
        tranca.setAnoDeFabricacao("2022");
        tranca.setModelo("Modelo X");
        tranca.setStatus(TrancaStatus.LIVRE);

        Tranca saved = trancaRepository.save(tranca);
        Optional<Tranca> found = trancaRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("A", found.get().getLocalizacao());
    }

    @Test
    @DisplayName("Deve deletar uma tranca")
    void testDelete() {
        Tranca tranca = new Tranca();
        tranca.setNumero(2);
        tranca.setLocalizacao("B");
        tranca.setAnoDeFabricacao("2021");
        tranca.setModelo("Modelo Y");
        tranca.setStatus(TrancaStatus.NOVA);

        Tranca saved = trancaRepository.save(tranca);
        Long id = saved.getId();

        trancaRepository.deleteById(id);

        Optional<Tranca> found = trancaRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve retornar lista de trancas")
    void testFindAll() {
        Tranca t1 = new Tranca();
        t1.setNumero(3);
        t1.setLocalizacao("C");
        t1.setAnoDeFabricacao("2020");
        t1.setModelo("Modelo Z");
        t1.setStatus(TrancaStatus.APOSENTADA);

        Tranca t2 = new Tranca();
        t2.setNumero(4);
        t2.setLocalizacao("D");
        t2.setAnoDeFabricacao("2019");
        t2.setModelo("Modelo W");
        t2.setStatus(TrancaStatus.EM_REPARO);

        trancaRepository.save(t1);
        trancaRepository.save(t2);

        assertTrue(trancaRepository.findAll().size() >= 2);
    }
}
