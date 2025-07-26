package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

class DestrancarRequestDTOTest {

    @Test
    void testSetAndGetIdBicicleta() {
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        dto.setBicicleta(1L);
        assertEquals(1L, dto.getBicicleta());
    }

    @Test
    void testEqualsAndHashCode() {
        DestrancarRequestDTO dto1 = new DestrancarRequestDTO();
        dto1.setBicicleta(1L);

        DestrancarRequestDTO dto2 = new DestrancarRequestDTO();
        dto2.setBicicleta(1L);

        DestrancarRequestDTO dto3 = new DestrancarRequestDTO();
        dto3.setBicicleta(2L);

        // Teste de igualdade
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        // Teste de hashCode
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        dto.setBicicleta(1L);
        String str = dto.toString();
        assertTrue(str.contains("idBicicleta=1"));
    }

    @Test
    void testNoArgsConstructor() {
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        assertNull(dto.getBicicleta());
    }
}