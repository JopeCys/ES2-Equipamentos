package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaMapperTest {

    @Test
    void testToDTO() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(100);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2022");
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        BicicletaResponseDTO dto = BicicletaMapper.toDTO(bicicleta);

        assertEquals(1L, dto.getId());
        assertEquals(100, dto.getNumero());
        assertEquals("Caloi", dto.getMarca());
        assertEquals("Elite", dto.getModelo());
        assertEquals("2022", dto.getAno());
        assertEquals(BicicletaStatus.DISPONIVEL, dto.getStatus());
    }

    @Test
    void testToEntity() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Monark");
        dto.setModelo("Classic");
        dto.setAno("2021");

        Bicicleta bicicleta = BicicletaMapper.toEntity(dto);

        assertNull(bicicleta.getId());
        assertNull(bicicleta.getNumero());
        assertEquals("Monark", bicicleta.getMarca());
        assertEquals("Classic", bicicleta.getModelo());
        assertEquals("2021", bicicleta.getAno());
        assertNull(bicicleta.getStatus());
    }
}
