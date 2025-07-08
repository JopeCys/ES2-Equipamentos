package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import static org.junit.jupiter.api.Assertions.*;

class TrancaResponseDTOTest {

    @Test
    void testSetAndGetId() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setId(100L);
        assertEquals(100L, dto.getId());
    }

    @Test
    void testSetAndGetBicicleta() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setBicicleta(200);
        assertEquals(200, dto.getBicicleta());
    }

    @Test
    void testSetAndGetNumero() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setNumero(10);
        assertEquals(10, dto.getNumero());
    }

    @Test
    void testSetAndGetLocalizacao() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setLocalizacao("Bloco B");
        assertEquals("Bloco B", dto.getLocalizacao());
    }

    @Test
    void testSetAndGetAnoDeFabricacao() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setAnoDeFabricacao("2021");
        assertEquals("2021", dto.getAnoDeFabricacao());
    }

    @Test
    void testSetAndGetModelo() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setModelo("Modelo Y");
        assertEquals("Modelo Y", dto.getModelo());
    }

    @Test
    void testSetAndGetStatus() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setStatus(TrancaStatus.LIVRE);
        assertEquals(TrancaStatus.LIVRE, dto.getStatus());
    }

    @Test
    void testAllArgs() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setId(1L);
        dto.setBicicleta(2);
        dto.setNumero(3);
        dto.setLocalizacao("C");
        dto.setAnoDeFabricacao("2020");
        dto.setModelo("M3");
        dto.setStatus(TrancaStatus.OCUPADA);

        assertAll(
            () -> assertEquals(1L, dto.getId()),
            () -> assertEquals(2, dto.getBicicleta()),
            () -> assertEquals(3, dto.getNumero()),
            () -> assertEquals("C", dto.getLocalizacao()),
            () -> assertEquals("2020", dto.getAnoDeFabricacao()),
            () -> assertEquals("M3", dto.getModelo()),
            () -> assertEquals(TrancaStatus.OCUPADA, dto.getStatus())
        );
    }

    @Test
    void testEqualsAndHashCode() {
        TrancaResponseDTO dto1 = new TrancaResponseDTO();
        dto1.setId(1L);
        dto1.setBicicleta(2);
        dto1.setNumero(3);
        dto1.setLocalizacao("D");
        dto1.setAnoDeFabricacao("2019");
        dto1.setModelo("M4");
        dto1.setStatus(TrancaStatus.NOVA);

        TrancaResponseDTO dto2 = new TrancaResponseDTO();
        dto2.setId(1L);
        dto2.setBicicleta(2);
        dto2.setNumero(3);
        dto2.setLocalizacao("D");
        dto2.setAnoDeFabricacao("2019");
        dto2.setModelo("M4");
        dto2.setStatus(TrancaStatus.NOVA);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setId(10L);
        dto.setBicicleta(20);
        dto.setNumero(30);
        dto.setLocalizacao("E");
        dto.setAnoDeFabricacao("2018");
        dto.setModelo("M5");
        dto.setStatus(TrancaStatus.APOSENTADA);

        String str = dto.toString();
        assertTrue(str.contains("id=10"));
        assertTrue(str.contains("bicicleta=20"));
        assertTrue(str.contains("numero=30"));
        assertTrue(str.contains("localizacao=E"));
        assertTrue(str.contains("anoDeFabricacao=2018"));
        assertTrue(str.contains("modelo=M5"));
        assertTrue(str.contains("status=APOSENTADA"));
    }
}