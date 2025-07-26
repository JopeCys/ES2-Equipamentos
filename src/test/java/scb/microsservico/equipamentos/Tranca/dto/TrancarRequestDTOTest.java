package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

class TrancarRequestDTOTest {

    @Test
    void testSettersAndGetters() {
        TrancarRequestDTO dto = new TrancarRequestDTO();
        dto.setBicicleta(1L);
        assertEquals(1L, dto.getBicicleta());
    }

    @Test
    void testEqualsAndHashCode() {
        TrancarRequestDTO dto1 = new TrancarRequestDTO();
        dto1.setBicicleta(1L);

        TrancarRequestDTO dto2 = new TrancarRequestDTO();
        dto2.setBicicleta(1L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancarRequestDTO dto = new TrancarRequestDTO();
        dto.setBicicleta(1L);

        String str = dto.toString();
        assertTrue(str.contains("idBicicleta=1"));
    }
}