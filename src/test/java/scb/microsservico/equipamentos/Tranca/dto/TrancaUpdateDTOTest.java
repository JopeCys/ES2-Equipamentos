package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;

import static org.junit.jupiter.api.Assertions.*;

class TrancaUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setAnoDeFabricacao("2023");
        dto.setModelo("Modelo Teste");

        assertEquals("2023", dto.getAnoDeFabricacao());
        assertEquals("Modelo Teste", dto.getModelo());
    }

    @Test
    void testEqualsAndHashCode() {
        TrancaUpdateDTO dto1 = new TrancaUpdateDTO();
        dto1.setAnoDeFabricacao("2023");
        dto1.setModelo("Modelo Teste");

        TrancaUpdateDTO dto2 = new TrancaUpdateDTO();
        dto2.setAnoDeFabricacao("2023");
        dto2.setModelo("Modelo Teste");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setAnoDeFabricacao("2023");
        dto.setModelo("Modelo Teste");

        String dtoAsString = dto.toString();

        assertTrue(dtoAsString.contains("anoDeFabricacao=2023"));
        assertTrue(dtoAsString.contains("modelo=Modelo Teste"));
    }
}