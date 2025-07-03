package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.repository.TotemRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TotemRepositoryTest {

    @Autowired
    private TotemRepository totemRepository;

    @Test
    @DisplayName("Deve salvar e buscar Totem por ID")
    void testSaveAndFindById() {
        Totem totem = new Totem();
        totem.setLocalizacao("Bloco Z");
        totem.setDescricao("Totem de integração");
        Totem saved = totemRepository.save(totem);

        Optional<Totem> found = totemRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Bloco Z", found.get().getLocalizacao());
        assertEquals("Totem de integração", found.get().getDescricao());
    }

    @Test
    @DisplayName("Deve retornar todos os Totens")
    void testFindAll() {
        Totem t1 = new Totem();
        t1.setLocalizacao("A");
        t1.setDescricao("desc1");
        Totem t2 = new Totem();
        t2.setLocalizacao("B");
        t2.setDescricao("desc2");
        totemRepository.save(t1);
        totemRepository.save(t2);

        List<Totem> all = totemRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    @DisplayName("Deve deletar Totem")
    void testDelete() {
        Totem totem = new Totem();
        totem.setLocalizacao("C");
        totem.setDescricao("desc3");
        Totem saved = totemRepository.save(totem);

        totemRepository.delete(saved);
        Optional<Totem> found = totemRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}
