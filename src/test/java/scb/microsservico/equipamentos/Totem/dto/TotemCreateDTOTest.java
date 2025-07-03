package scb.microsservico.equipamentos.Totem.dto;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import static org.assertj.core.api.Assertions.assertThat;

public class TotemCreateDTOTest {

    @Test
    void testSetAndGetLocalizacao() {
        TotemCreateDTO dto = new TotemCreateDTO();
        dto.setLocalizacao("Bloco A");
        assertThat(dto.getLocalizacao()).isEqualTo("Bloco A");
    }

    @Test
    void testSetAndGetDescricao() {
        TotemCreateDTO dto = new TotemCreateDTO();
        dto.setDescricao("Totem de entrada");
        assertThat(dto.getDescricao()).isEqualTo("Totem de entrada");
    }

    @Test
    void testEqualsAndHashCode() {
        TotemCreateDTO dto1 = new TotemCreateDTO();
        dto1.setLocalizacao("L1");
        dto1.setDescricao("D1");

        TotemCreateDTO dto2 = new TotemCreateDTO();
        dto2.setLocalizacao("L1");
        dto2.setDescricao("D1");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        TotemCreateDTO dto = new TotemCreateDTO();
        dto.setLocalizacao("L2");
        dto.setDescricao("D2");
        String str = dto.toString();
        assertThat(str).contains("localizacao=L2");
        assertThat(str).contains("descricao=D2");
    }
}
