package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BicicletaRepositoryTest {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Test
    @DisplayName("Deve salvar uma bicicleta no banco de dados")
    void testSaveBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2022");

        Bicicleta saved = bicicletaRepository.save(bicicleta);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMarca()).isEqualTo("Caloi");
        assertThat(saved.getModelo()).isEqualTo("Elite");
        assertThat(saved.getAno()).isEqualTo("2022");
    }

    @Test
    @DisplayName("Deve buscar uma bicicleta por ID")
    void testFindById() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Monark");
        bicicleta.setModelo("Classic");
        bicicleta.setAno("2020");
        Bicicleta saved = bicicletaRepository.save(bicicleta);

        Optional<Bicicleta> found = bicicletaRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMarca()).isEqualTo("Monark");
        assertThat(found.get().getAno()).isEqualTo("2020");
    }

    @Test
    @DisplayName("Deve deletar uma bicicleta")
    void testDeleteBicicleta() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Sense");
        bicicleta.setModelo("Impact");
        bicicleta.setAno("2021");
        Bicicleta saved = bicicletaRepository.save(bicicleta);

        bicicletaRepository.delete(saved);

        Optional<Bicicleta> found = bicicletaRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Deve listar todas as bicicletas")
    void testFindAll() {
        Bicicleta b1 = new Bicicleta();
        b1.setMarca("Caloi");
        b1.setModelo("City Tour");
        b1.setAno("2019");

        Bicicleta b2 = new Bicicleta();
        b2.setMarca("Monark");
        b2.setModelo("Barra Forte");
        b2.setAno("2018");

        bicicletaRepository.save(b1);
        bicicletaRepository.save(b2);

        assertThat(bicicletaRepository.findAll()).hasSize(2);
    }
}
