package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BicicletaRepositoryTest {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Test
    @DisplayName("Deve salvar e buscar uma bicicleta pelo ID")
    void testSaveAndFindById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(12345);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("10");
        bicicleta.setAno("2020");
        bicicleta.setStatus(BicicletaStatus.NOVA);

        Bicicleta saved = bicicletaRepository.save(bicicleta);
        Optional<Bicicleta> found = bicicletaRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(12345, found.get().getNumero());
    }

    @Test
    @DisplayName("Deve retornar todas as bicicletas")
    void testFindAll() {
        Bicicleta b1 = new Bicicleta();
        b1.setNumero(1);
        b1.setMarca("Marca A");
        b1.setModelo("Modelo A");
        b1.setAno("2021");
        b1.setStatus(BicicletaStatus.DISPONIVEL);

        Bicicleta b2 = new Bicicleta();
        b2.setNumero(2);
        b2.setMarca("Marca B");
        b2.setModelo("Modelo B");
        b2.setAno("2022");
        b2.setStatus(BicicletaStatus.EM_USO);

        bicicletaRepository.save(b1);
        bicicletaRepository.save(b2);

        List<Bicicleta> all = bicicletaRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    @DisplayName("Deve deletar uma bicicleta")
    void testDelete() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(54321);
        bicicleta.setMarca("Monark");
        bicicleta.setModelo("Classic");
        bicicleta.setAno("2023");
        bicicleta.setStatus(BicicletaStatus.APOSENTADA);

        Bicicleta saved = bicicletaRepository.save(bicicleta);
        Long id = saved.getId();

        bicicletaRepository.deleteById(id);

        Optional<Bicicleta> found = bicicletaRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve verificar se uma bicicleta existe pelo número")
    void testExistsByNumero() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(98765);
        bicicleta.setMarca("Sense");
        bicicleta.setModelo("Impact");
        bicicleta.setAno("2024");
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta);

        assertTrue(bicicletaRepository.existsByNumero(98765));
        assertFalse(bicicletaRepository.existsByNumero(11111));
    }

    @Test
    @DisplayName("Deve encontrar uma bicicleta pelo número")
    void testFindByNumero() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(11223);
        bicicleta.setMarca("Oggi");
        bicicleta.setModelo("Big Wheel");
        bicicleta.setAno("2025");
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta);

        Optional<Bicicleta> found = bicicletaRepository.findByNumero(11223);
        assertTrue(found.isPresent());
        assertEquals(11223, found.get().getNumero());
    }

    @Test
    @DisplayName("Deve encontrar todas as bicicletas com números específicos")
    void testFindAllByNumeroIn() {
        Bicicleta b1 = new Bicicleta();
        b1.setNumero(101);
        b1.setMarca("Scott");
        b1.setModelo("Spark");
        b1.setAno("2023");
        b1.setStatus(BicicletaStatus.NOVA);

        Bicicleta b2 = new Bicicleta();
        b2.setNumero(102);
        b2.setMarca("Trek");
        b2.setModelo("Marlin");
        b2.setAno("2023");
        b2.setStatus(BicicletaStatus.NOVA);

        bicicletaRepository.save(b1);
        bicicletaRepository.save(b2);

        List<Integer> numeros = Arrays.asList(101, 102);
        List<Bicicleta> encontradas = bicicletaRepository.findAllByNumeroIn(numeros);

        assertEquals(2, encontradas.size());
    }
}