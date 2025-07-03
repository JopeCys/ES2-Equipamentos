package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;

import static org.junit.jupiter.api.Assertions.*;

public class TrancaUpdateDTOTest {

    @Test
    void testSetAndGetLocalizacao() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setLocalizacao("Rua A, 123");
        assertEquals("Rua A, 123", dto.getLocalizacao());
    }

    @Test
    void testSetAndGetAnoDeFabricacao() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setAnoDeFabricacao("2022");
        assertEquals("2022", dto.getAnoDeFabricacao());
    }

    @Test
    void testSetAndGetModelo() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setModelo("Modelo X");
        assertEquals("Modelo X", dto.getModelo());
    }

    @Test
    void testAllArgs() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setLocalizacao("Rua B, 456");
        dto.setAnoDeFabricacao("2021");
        dto.setModelo("Modelo Y");

        assertEquals("Rua B, 456", dto.getLocalizacao());
        assertEquals("2021", dto.getAnoDeFabricacao());
        assertEquals("Modelo Y", dto.getModelo());
    }

    @Test
    void testEqualsAndHashCode() {
        TrancaUpdateDTO dto1 = new TrancaUpdateDTO();
        dto1.setLocalizacao("Rua C");
        dto1.setAnoDeFabricacao("2020");
        dto1.setModelo("Modelo Z");

        TrancaUpdateDTO dto2 = new TrancaUpdateDTO();
        dto2.setLocalizacao("Rua C");
        dto2.setAnoDeFabricacao("2020");
        dto2.setModelo("Modelo Z");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setLocalizacao("Rua D");
        dto.setAnoDeFabricacao("2019");
        dto.setModelo("Modelo W");

        String toString = dto.toString();
        assertTrue(toString.contains("Rua D"));
        assertTrue(toString.contains("2019"));
        assertTrue(toString.contains("Modelo W"));
    }
}
