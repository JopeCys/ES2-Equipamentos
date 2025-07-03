package scb.microsservico.equipamentos.Totem.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import static org.junit.jupiter.api.Assertions.*;

class TotemUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        TotemUpdateDTO dto = new TotemUpdateDTO();
        dto.setLocalizacao("Bloco A");
        dto.setDescricao("Totem próximo à entrada principal");

        assertEquals("Bloco A", dto.getLocalizacao());
        assertEquals("Totem próximo à entrada principal", dto.getDescricao());
    }

    @Test
    void testNoArgsConstructor() {
        TotemUpdateDTO dto = new TotemUpdateDTO();
        assertNull(dto.getLocalizacao());
        assertNull(dto.getDescricao());
    }

    @Test
    void testEqualsAndHashCode() {
        TotemUpdateDTO dto1 = new TotemUpdateDTO();
        dto1.setLocalizacao("Bloco B");
        dto1.setDescricao("Totem secundário");

        TotemUpdateDTO dto2 = new TotemUpdateDTO();
        dto2.setLocalizacao("Bloco B");
        dto2.setDescricao("Totem secundário");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TotemUpdateDTO dto = new TotemUpdateDTO();
        dto.setLocalizacao("Bloco C");
        dto.setDescricao("Totem de teste");

        String toString = dto.toString();
        assertTrue(toString.contains("localizacao=Bloco C"));
        assertTrue(toString.contains("descricao=Totem de teste"));
    }
}