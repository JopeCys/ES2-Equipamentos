package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import static org.junit.jupiter.api.Assertions.*;

public class BicicletaTest {

    @Test
    void testBicicletaSettersAndGetters() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(123);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2022");
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        assertEquals(1L, bicicleta.getId());
        assertEquals(123, bicicleta.getNumero());
        assertEquals("Caloi", bicicleta.getMarca());
        assertEquals("Elite", bicicleta.getModelo());
        assertEquals("2022", bicicleta.getAno());
        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
    }

    @Test
    void testBicicletaEqualsAndHashCode() {
        Bicicleta b1 = new Bicicleta();
        b1.setId(1L);
        b1.setNumero(10);

        Bicicleta b2 = new Bicicleta();
        b2.setId(1L);
        b2.setNumero(10);

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testBicicletaToString() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(2L);
        bicicleta.setNumero(456);
        bicicleta.setMarca("Monark");
        bicicleta.setModelo("Classic");
        bicicleta.setAno("2021");
        bicicleta.setStatus(BicicletaStatus.EM_USO);

        String toString = bicicleta.toString();
        assertTrue(toString.contains("id=2"));
        assertTrue(toString.contains("numero=456"));
        assertTrue(toString.contains("marca=Monark"));
        assertTrue(toString.contains("modelo=Classic"));
        assertTrue(toString.contains("ano=2021"));
        assertTrue(toString.contains("status=EM_USO"));
    }
}
