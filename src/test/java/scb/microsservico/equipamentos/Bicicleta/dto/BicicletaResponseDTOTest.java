package scb.microsservico.equipamentos.Bicicleta.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaResponseDTOTest {

    @Test
    void testSettersAndGetters() {
        BicicletaResponseDTO dto = new BicicletaResponseDTO();
        dto.setId(10L);
        dto.setNumero(200);
        dto.setMarca("Caloi");
        dto.setModelo("Elite");
        dto.setAno("2024");
        dto.setStatus(BicicletaStatus.DISPONIVEL);
        dto.setLocalizacao("Estação Leste");

        assertEquals(10L, dto.getId());
        assertEquals(200, dto.getNumero());
        assertEquals("Caloi", dto.getMarca());
        assertEquals("Elite", dto.getModelo());
        assertEquals("2024", dto.getAno());
        assertEquals(BicicletaStatus.DISPONIVEL, dto.getStatus());
        assertEquals("Estação Leste", dto.getLocalizacao());
    }

    @Test
    void testEqualsAndHashCode() {
        BicicletaResponseDTO dto1 = new BicicletaResponseDTO();
        dto1.setId(1L);
        dto1.setNumero(100);
        dto1.setMarca("Monark");
        dto1.setModelo("Classic");
        dto1.setAno("2022");
        dto1.setStatus(BicicletaStatus.EM_USO);
        dto1.setLocalizacao("Estação Norte");

        BicicletaResponseDTO dto2 = new BicicletaResponseDTO();
        dto2.setId(1L);
        dto2.setNumero(100);
        dto2.setMarca("Monark");
        dto2.setModelo("Classic");
        dto2.setAno("2022");
        dto2.setStatus(BicicletaStatus.EM_USO);
        dto2.setLocalizacao("Estação Norte");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BicicletaResponseDTO dto = new BicicletaResponseDTO();
        dto.setId(5L);
        dto.setNumero(50);
        dto.setMarca("Sense");
        dto.setModelo("Impact");
        dto.setAno("2021");
        dto.setStatus(BicicletaStatus.APOSENTADA);
        dto.setLocalizacao("Estação Sul");

        String str = dto.toString();
        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("numero=50"));
        assertTrue(str.contains("marca=Sense"));
        assertTrue(str.contains("modelo=Impact"));
        assertTrue(str.contains("ano=2021"));
        assertTrue(str.contains("status=APOSENTADA"));
        assertTrue(str.contains("localizacao=Estação Sul"));
    }
}
