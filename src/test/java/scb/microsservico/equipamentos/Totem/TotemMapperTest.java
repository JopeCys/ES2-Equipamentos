package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.mapper.TotemMapper;
import scb.microsservico.equipamentos.model.Totem;
import static org.junit.jupiter.api.Assertions.*;

class TotemMapperTest {

    @Test
    void testToDTO() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Bloco A");
        totem.setDescricao("Totem principal");

        TotemResponseDTO dto = TotemMapper.toDTO(totem);

        assertEquals(1L, dto.getId());
        assertEquals("Bloco A", dto.getLocalizacao());
        assertEquals("Totem principal", dto.getDescricao());
    }

    @Test
    void testToEntity() {
        TotemCreateDTO createDTO = new TotemCreateDTO();
        createDTO.setLocalizacao("Bloco B");
        createDTO.setDescricao("Totem secundário");

        Totem totem = TotemMapper.toEntity(createDTO);

        assertNull(totem.getId());
        assertEquals("Bloco B", totem.getLocalizacao());
        assertEquals("Totem secundário", totem.getDescricao());
    }

    @Test
    void testToDTOWithNull() {
        assertThrows(NullPointerException.class, () -> TotemMapper.toDTO(null));
    }

    @Test
    void testToEntityWithNull() {
        assertThrows(NullPointerException.class, () -> TotemMapper.toEntity(null));
    }
}
