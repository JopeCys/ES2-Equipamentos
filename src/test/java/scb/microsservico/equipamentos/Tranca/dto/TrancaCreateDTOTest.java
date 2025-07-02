package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.TrancaCreateDTO;
import static org.junit.jupiter.api.Assertions.*;


class TrancaCreateDTOTest {

    @Test
    void testSetAndGetNumero() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(123);
        assertEquals(123, dto.getNumero());
    }

    @Test
    void testSetAndGetLocalizacao() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setLocalizacao("Bloco A");
        assertEquals("Bloco A", dto.getLocalizacao());
    }

    @Test
    void testSetAndGetAnoDeFabricacao() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setAnoDeFabricacao("2023");
        assertEquals("2023", dto.getAnoDeFabricacao());
    }

    @Test
    void testSetAndGetModelo() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setModelo("Modelo X");
        assertEquals("Modelo X", dto.getModelo());
    }

    @Test
    void testAllArgs() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(10);
        dto.setLocalizacao("Sala 2");
        dto.setAnoDeFabricacao("2022");
        dto.setModelo("Tranca Pro");

        assertAll(
            () -> assertEquals(10, dto.getNumero()),
            () -> assertEquals("Sala 2", dto.getLocalizacao()),
            () -> assertEquals("2022", dto.getAnoDeFabricacao()),
            () -> assertEquals("Tranca Pro", dto.getModelo())
        );
    }

    @Test
    void testEqualsAndHashCode() {
        TrancaCreateDTO dto1 = new TrancaCreateDTO();
        dto1.setNumero(1);
        dto1.setLocalizacao("A");
        dto1.setAnoDeFabricacao("2020");
        dto1.setModelo("M1");

        TrancaCreateDTO dto2 = new TrancaCreateDTO();
        dto2.setNumero(1);
        dto2.setLocalizacao("A");
        dto2.setAnoDeFabricacao("2020");
        dto2.setModelo("M1");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(5);
        dto.setLocalizacao("B");
        dto.setAnoDeFabricacao("2021");
        dto.setModelo("M2");

        String str = dto.toString();
        assertTrue(str.contains("numero=5"));
        assertTrue(str.contains("localizacao=B"));
        assertTrue(str.contains("anoDeFabricacao=2021"));
        assertTrue(str.contains("modelo=M2"));
    }
}