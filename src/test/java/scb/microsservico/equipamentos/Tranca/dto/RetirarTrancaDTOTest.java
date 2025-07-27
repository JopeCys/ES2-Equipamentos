package scb.microsservico.equipamentos.Tranca.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Tranca.RetirarTrancaDTO;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import static org.junit.jupiter.api.Assertions.*;

public class RetirarTrancaDTOTest {

    @Test
    void testGettersAndSetters() {
        RetirarTrancaDTO dto = new RetirarTrancaDTO();
        dto.setIdTranca(1L);
        dto.setIdTotem(2L);
        dto.setIdFuncionario(3L);
        dto.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        assertEquals(1L, dto.getIdTranca());
        assertEquals(2L, dto.getIdTotem());
        assertEquals(3L, dto.getIdFuncionario());
        assertEquals(AcaoRetirar.EM_REPARO, dto.getStatusAcaoReparador());
    }

    @Test
    void testEqualsAndHashCode() {
        RetirarTrancaDTO dto1 = new RetirarTrancaDTO();
        dto1.setIdTranca(1L);
        dto1.setIdTotem(2L);
        dto1.setIdFuncionario(3L);
        dto1.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        RetirarTrancaDTO dto2 = new RetirarTrancaDTO();
        dto2.setIdTranca(1L);
        dto2.setIdTotem(2L);
        dto2.setIdFuncionario(3L);
        dto2.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RetirarTrancaDTO dto = new RetirarTrancaDTO();
        dto.setIdTranca(1L);
        dto.setIdTotem(2L);
        dto.setIdFuncionario(3L);
        dto.setStatusAcaoReparador(AcaoRetirar.APOSENTADA);

        String str = dto.toString();

        assertTrue(str.contains("idTranca=1"));
        assertTrue(str.contains("idTotem=2"));
        assertTrue(str.contains("idFuncionario=3"));
        assertTrue(str.contains("statusAcaoReparador=APOSENTADORIA"));
    }
}