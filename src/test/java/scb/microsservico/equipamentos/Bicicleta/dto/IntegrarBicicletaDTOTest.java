package scb.microsservico.equipamentos.Bicicleta.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrarBicicletaDTOTest {

    @Test
    void testSettersAndGetters() {
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(2L);
        dto.setIdFuncionario(3L);

        assertEquals(1L, dto.getIdTranca());
        assertEquals(2L, dto.getIdBicicleta());
        assertEquals(3L, dto.getIdFuncionario());
    }

    @Test
    void testEqualsAndHashCode() {
        IntegrarBicicletaDTO dto1 = new IntegrarBicicletaDTO();
        dto1.setIdTranca(1L);
        dto1.setIdBicicleta(2L);
        dto1.setIdFuncionario(3L);

        IntegrarBicicletaDTO dto2 = new IntegrarBicicletaDTO();
        dto2.setIdTranca(1L);
        dto2.setIdBicicleta(2L);
        dto2.setIdFuncionario(3L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(2L);
        dto.setIdFuncionario(3L);

        String str = dto.toString();
        assertTrue(str.contains("idTranca=1"));
        assertTrue(str.contains("idBicicleta=2"));
        assertTrue(str.contains("idFuncionario=3"));
    }
}