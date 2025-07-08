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
        tranca.setBicicleta(2);
        tranca.setNumero(3);
        tranca.setLocalizacao("Local");
        tranca.setAnoDeFabricacao("2022");
        tranca.setModelo("Modelo X");
        tranca.setStatus(TrancaStatus.LIVRE);

        TrancaResponseDTO dto = TrancaMapper.toDTO(tranca);

        assertEquals(1L, dto.getId());
        assertEquals(2, dto.getBicicleta());
        assertEquals(3, dto.getNumero());
        assertEquals("Local", dto.getLocalizacao());
        assertEquals("2022", dto.getAnoDeFabricacao());
        assertEquals("Modelo X", dto.getModelo());
        assertEquals(TrancaStatus.LIVRE, dto.getStatus());
    }

    @Test
    void testToEntity() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(10);
        dto.setLocalizacao("Sala 2");
        dto.setAnoDeFabricacao("2021");
        dto.setModelo("Tranca Pro");

        Tranca tranca = TrancaMapper.toEntity(dto);

        assertNull(tranca.getId());
        assertNull(tranca.getBicicleta());
        assertEquals(10, tranca.getNumero());
        assertEquals("Sala 2", tranca.getLocalizacao());
        assertEquals("2021", tranca.getAnoDeFabricacao());
        assertEquals("Tranca Pro", tranca.getModelo());
    }
}
