package scb.microsservico.equipamentos.Totem.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import static org.assertj.core.api.Assertions.assertThat;

public class TotemResponseDTOTest {

    @Test
    void testSetAndGetId() {
        TotemResponseDTO dto = new TotemResponseDTO();
        dto.setId(10L);
        assertThat(dto.getId()).isEqualTo(10L);
    }

    @Test
    void testSetAndGetLocalizacao() {
        TotemResponseDTO dto = new TotemResponseDTO();
        dto.setLocalizacao("Bloco B");
        assertThat(dto.getLocalizacao()).isEqualTo("Bloco B");
    }

    @Test
    void testSetAndGetDescricao() {
        TotemResponseDTO dto = new TotemResponseDTO();
        dto.setDescricao("Totem de saída");
        assertThat(dto.getDescricao()).isEqualTo("Totem de saída");
    }

    @Test
    void testEqualsAndHashCode() {
        TotemResponseDTO dto1 = new TotemResponseDTO();
        dto1.setId(1L);
        dto1.setLocalizacao("L1");
        dto1.setDescricao("D1");

        TotemResponseDTO dto2 = new TotemResponseDTO();
        dto2.setId(1L);
        dto2.setLocalizacao("L1");
        dto2.setDescricao("D1");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        TotemResponseDTO dto = new TotemResponseDTO();
        dto.setId(2L);
        dto.setLocalizacao("L2");
        dto.setDescricao("D2");
        String str = dto.toString();
        assertThat(str).contains("id=2");
        assertThat(str).contains("localizacao=L2");
        assertThat(str).contains("descricao=D2");
    }
}
