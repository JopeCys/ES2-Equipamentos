package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Tranca;

import static org.junit.jupiter.api.Assertions.*;

class TrancaMapperTest {

    @Test
    void testToDTO() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setBicicleta(123);
        tranca.setNumero(456);
        tranca.setLocalizacao("Bloco A");
        tranca.setAnoDeFabricacao("2023");
        tranca.setModelo("Modelo Teste");
        tranca.setStatus(TrancaStatus.LIVRE);

        TrancaResponseDTO dto = TrancaMapper.toDTO(tranca);

        assertEquals(1L, dto.getId());
        assertEquals(123, dto.getBicicleta());
        assertEquals(456, dto.getNumero());
        assertEquals("Bloco A", dto.getLocalizacao());
        assertEquals("2023", dto.getAnoDeFabricacao());
        assertEquals("Modelo Teste", dto.getModelo());
        assertEquals(TrancaStatus.LIVRE, dto.getStatus());
    }

    @Test
    void testToEntity() {
        TrancaCreateDTO createDTO = new TrancaCreateDTO();
        createDTO.setNumero(789);
        createDTO.setAnoDeFabricacao("2024");
        createDTO.setModelo("Modelo Novo");

        Tranca tranca = TrancaMapper.toEntity(createDTO);

        assertNull(tranca.getId());
        assertEquals(789, tranca.getNumero());
        assertEquals("2024", tranca.getAnoDeFabricacao());
        assertEquals("Modelo Novo", tranca.getModelo());
        assertNull(tranca.getStatus());
        assertNull(tranca.getBicicleta());
        assertNull(tranca.getLocalizacao());
    }

    @Test
    void testToDTOWithNull() {
        assertThrows(NullPointerException.class, () -> TrancaMapper.toDTO(null));
    }

    @Test
    void testToEntityWithNull() {
        assertThrows(NullPointerException.class, () -> TrancaMapper.toEntity(null));
    }
}