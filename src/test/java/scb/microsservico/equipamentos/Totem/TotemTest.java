package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class TotemTest {

    @Test
    void testGettersAndSetters() {
        Totem totem = new Totem();
        totem.setId(10L);
        totem.setLocalizacao("Bloco X");
        totem.setDescricao("Totem de Teste");

        assertEquals(10L, totem.getId());
        assertEquals("Bloco X", totem.getLocalizacao());
        assertEquals("Totem de Teste", totem.getDescricao());
    }

    @Test
    void testTrancasList() {
        Totem totem = new Totem();
        Tranca tranca1 = new Tranca();
        Tranca tranca2 = new Tranca();
        totem.setTrancas(Arrays.asList(tranca1, tranca2));

        assertNotNull(totem.getTrancas());
        assertEquals(2, totem.getTrancas().size());
        assertTrue(totem.getTrancas().contains(tranca1));
        assertTrue(totem.getTrancas().contains(tranca2));
    }

    @Test
    void testNoArgsConstructor() {
        Totem totem = new Totem();
        assertNull(totem.getId());
        assertNull(totem.getLocalizacao());
        assertNull(totem.getDescricao());
        assertTrue(totem.getTrancas().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Totem t1 = new Totem();
        t1.setId(1L);
        t1.setLocalizacao("A");
        t1.setDescricao("desc");
        t1.setTrancas(Collections.emptyList());

        Totem t2 = new Totem();
        t2.setId(1L);
        t2.setLocalizacao("A");
        t2.setDescricao("desc");
        t2.setTrancas(Collections.emptyList());

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        Totem totem = new Totem();
        totem.setId(5L);
        totem.setLocalizacao("Bloco Y");
        totem.setDescricao("Totem para testes");
        String str = totem.toString();
        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("localizacao=Bloco Y"));
        assertTrue(str.contains("descricao=Totem para testes"));
    }
}
