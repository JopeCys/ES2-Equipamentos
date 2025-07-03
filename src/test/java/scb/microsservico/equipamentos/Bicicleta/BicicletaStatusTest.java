package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.enums.BicicletaStatus;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaStatusTest {

    @Test
    void testEnumValues() {
        BicicletaStatus[] values = BicicletaStatus.values();
        assertArrayEquals(
            new BicicletaStatus[]{
                BicicletaStatus.DISPONIVEL,
                BicicletaStatus.EM_USO,
                BicicletaStatus.NOVA,
                BicicletaStatus.APOSENTADA,
                BicicletaStatus.REPARO_SOLICITADO,
                BicicletaStatus.EM_REPARO
            },
            values
        );
    }

    @Test
    void testValueOf() {
        assertEquals(BicicletaStatus.DISPONIVEL, BicicletaStatus.valueOf("DISPONIVEL"));
        assertEquals(BicicletaStatus.EM_USO, BicicletaStatus.valueOf("EM_USO"));
        assertEquals(BicicletaStatus.NOVA, BicicletaStatus.valueOf("NOVA"));
        assertEquals(BicicletaStatus.APOSENTADA, BicicletaStatus.valueOf("APOSENTADA"));
        assertEquals(BicicletaStatus.REPARO_SOLICITADO, BicicletaStatus.valueOf("REPARO_SOLICITADO"));
        assertEquals(BicicletaStatus.EM_REPARO, BicicletaStatus.valueOf("EM_REPARO"));
    }
}
