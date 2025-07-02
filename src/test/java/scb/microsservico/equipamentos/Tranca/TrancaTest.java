package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Tranca;
import static org.junit.jupiter.api.Assertions.*;

class TrancaTest {

    @Test
    void testSetAndGetId() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        assertEquals(1L, tranca.getId());
    }

    @Test
    void testSetAndGetBicicleta() {
        Tranca tranca = new Tranca();
        tranca.setBicicleta(2L);
        assertEquals(2L, tranca.getBicicleta());
    }

    @Test
    void testSetAndGetNumero() {
        Tranca tranca = new Tranca();
        tranca.setNumero(10);
        assertEquals(10, tranca.getNumero());
    }

    @Test
    void testSetAndGetLocalizacao() {
        Tranca tranca = new Tranca();
        tranca.setLocalizacao("Bloco C");
        assertEquals("Bloco C", tranca.getLocalizacao());
    }

    @Test
    void testSetAndGetAnoDeFabricacao() {
        Tranca tranca = new Tranca();
        tranca.setAnoDeFabricacao("2023");
        assertEquals("2023", tranca.getAnoDeFabricacao());
    }

    @Test
    void testSetAndGetModelo() {
        Tranca tranca = new Tranca();
        tranca.setModelo("Modelo Z");
        assertEquals("Modelo Z", tranca.getModelo());
    }

    @Test
    void testSetAndGetStatus() {
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Tranca t1 = new Tranca();
        t1.setId(1L);
        t1.setBicicleta(2L);
        t1.setNumero(3);
        t1.setLocalizacao("A");
        t1.setAnoDeFabricacao("2020");
        t1.setModelo("M1");
        t1.setStatus(TrancaStatus.NOVA);

        Tranca t2 = new Tranca();
        t2.setId(1L);
        t2.setBicicleta(2L);
        t2.setNumero(3);
        t2.setLocalizacao("A");
        t2.setAnoDeFabricacao("2020");
        t2.setModelo("M1");
        t2.setStatus(TrancaStatus.NOVA);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        Tranca tranca = new Tranca();
        tranca.setId(5L);
        tranca.setBicicleta(6L);
        tranca.setNumero(7);
        tranca.setLocalizacao("D");
        tranca.setAnoDeFabricacao("2022");
        tranca.setModelo("M2");
        tranca.setStatus(TrancaStatus.APOSENTADA);

        String str = tranca.toString();
        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("bicicleta=6"));
        assertTrue(str.contains("numero=7"));
        assertTrue(str.contains("localizacao=D"));
        assertTrue(str.contains("anoDeFabricacao=2022"));
        assertTrue(str.contains("modelo=M2"));
        assertTrue(str.contains("status=APOSENTADA"));
    }
}
