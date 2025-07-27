package scb.microsservico.equipamentos.Bicicleta.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.AcaoRetirar;

import static org.junit.jupiter.api.Assertions.*;

class RetirarBicicletaDTOTest {

    @Test
    void testGettersAndSetters() {
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(2L);
        dto.setIdFuncionario(3L);
        dto.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        assertEquals(1L, dto.getIdTranca());
        assertEquals(2L, dto.getIdBicicleta());
        assertEquals(3L, dto.getIdFuncionario());
        assertEquals(AcaoRetirar.EM_REPARO, dto.getStatusAcaoReparador());
    }

    @Test
    void testEqualsAndHashCode() {
        RetirarBicicletaDTO dto1 = new RetirarBicicletaDTO();
        dto1.setIdTranca(1L);
        dto1.setIdBicicleta(2L);
        dto1.setIdFuncionario(3L);
        dto1.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        RetirarBicicletaDTO dto2 = new RetirarBicicletaDTO();
        dto2.setIdTranca(1L);
        dto2.setIdBicicleta(2L);
        dto2.setIdFuncionario(3L);
        dto2.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(2L);
        dto.setIdFuncionario(3L);
        dto.setStatusAcaoReparador(AcaoRetirar.APOSENTADA);

        String str = dto.toString();
        assertTrue(str.contains("idTranca=1"));
        assertTrue(str.contains("idBicicleta=2"));
        assertTrue(str.contains("idFuncionario=3"));
        assertTrue(str.contains("statusAcaoReparador=APOSENTADORIA"));
    }
}