package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.dto.Tranca.IntegrarTrancaDTO;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrarTrancaDTOTest {

    @Test
    void testSettersAndGetters() {
        IntegrarTrancaDTO dto = new IntegrarTrancaDTO();
        dto.setIdTranca(1L);
        dto.setIdTotem(10L);
        dto.setIdFuncionario(100L);

        assertEquals(1L, dto.getIdTranca());
        assertEquals(10L, dto.getIdTotem());
        assertEquals(100L, dto.getIdFuncionario());
    }

    @Test
    void testEqualsAndHashCode() {
        IntegrarTrancaDTO dto1 = new IntegrarTrancaDTO();
        dto1.setIdTranca(1L);
        dto1.setIdTotem(10L);
        dto1.setIdFuncionario(100L);

        IntegrarTrancaDTO dto2 = new IntegrarTrancaDTO();
        dto2.setIdTranca(1L);
        dto2.setIdTotem(10L);
        dto2.setIdFuncionario(100L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IntegrarTrancaDTO dto = new IntegrarTrancaDTO();
        dto.setIdTranca(1L);
        dto.setIdTotem(10L);
        dto.setIdFuncionario(100L);

        String str = dto.toString();

        assertTrue(str.contains("idTranca=1"));
        assertTrue(str.contains("idTotem=10"));
        assertTrue(str.contains("idFuncionario=100"));
    }
}