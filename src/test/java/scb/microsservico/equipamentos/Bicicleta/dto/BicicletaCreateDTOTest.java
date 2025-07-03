package scb.microsservico.equipamentos.Bicicleta.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaCreateDTOTest {

    @Test
    void testSettersAndGetters() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setNumero(101);
        dto.setMarca("Caloi");
        dto.setModelo("Elite");
        dto.setAno("2023");
        dto.setLocalizacao("Estação Central");

        assertEquals(101, dto.getNumero());
        assertEquals("Caloi", dto.getMarca());
        assertEquals("Elite", dto.getModelo());
        assertEquals("2023", dto.getAno());
        assertEquals("Estação Central", dto.getLocalizacao());
    }

    @Test
    void testEqualsAndHashCode() {
        BicicletaCreateDTO dto1 = new BicicletaCreateDTO();
        dto1.setNumero(1);
        dto1.setMarca("Monark");
        dto1.setModelo("Classic");
        dto1.setAno("2022");
        dto1.setLocalizacao("Estação Norte");

        BicicletaCreateDTO dto2 = new BicicletaCreateDTO();
        dto2.setNumero(1);
        dto2.setMarca("Monark");
        dto2.setModelo("Classic");
        dto2.setAno("2022");
        dto2.setLocalizacao("Estação Norte");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setNumero(5);
        dto.setMarca("Sense");
        dto.setModelo("Impact");
        dto.setAno("2021");
        dto.setLocalizacao("Estação Sul");

        String str = dto.toString();
        assertTrue(str.contains("numero=5"));
        assertTrue(str.contains("marca=Sense"));
        assertTrue(str.contains("modelo=Impact"));
        assertTrue(str.contains("ano=2021"));
        assertTrue(str.contains("localizacao=Estação Sul"));
    }
}
