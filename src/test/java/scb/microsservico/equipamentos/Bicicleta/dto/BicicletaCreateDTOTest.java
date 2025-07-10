package scb.microsservico.equipamentos.Bicicleta.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaCreateDTOTest {

    @Test
    void testSettersAndGetters() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Caloi");
        dto.setModelo("Elite");
        dto.setAno("2024");

        assertEquals("Caloi", dto.getMarca());
        assertEquals("Elite", dto.getModelo());
        assertEquals("2024", dto.getAno());
    }

    @Test
    void testEqualsAndHashCode() {
        BicicletaCreateDTO dto1 = new BicicletaCreateDTO();
        dto1.setMarca("Monark");
        dto1.setModelo("Classic");
        dto1.setAno("2022");

        BicicletaCreateDTO dto2 = new BicicletaCreateDTO();
        dto2.setMarca("Monark");
        dto2.setModelo("Classic");
        dto2.setAno("2022");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Sense");
        dto.setModelo("Impact");
        dto.setAno("2021");

        String str = dto.toString();
        assertTrue(str.contains("marca=Sense"));
        assertTrue(str.contains("modelo=Impact"));
        assertTrue(str.contains("ano=2021"));
    }
}