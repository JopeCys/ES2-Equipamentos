package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import static org.junit.jupiter.api.Assertions.*;

class TrancaStatusTest {

    @Test
    void testValues() {
        TrancaStatus[] values = TrancaStatus.values();
        assertEquals(5, values.length);
        assertArrayEquals(
            new TrancaStatus[]{
                TrancaStatus.LIVRE,
                TrancaStatus.OCUPADA,
                TrancaStatus.NOVA,
                TrancaStatus.APOSENTADA,
                TrancaStatus.EM_REPARO
            },
            values
        );
    }

    @Test
    void testValueOf() {
        assertEquals(TrancaStatus.LIVRE, TrancaStatus.valueOf("LIVRE"));
        assertEquals(TrancaStatus.OCUPADA, TrancaStatus.valueOf("OCUPADA"));
        assertEquals(TrancaStatus.NOVA, TrancaStatus.valueOf("NOVA"));
        assertEquals(TrancaStatus.APOSENTADA, TrancaStatus.valueOf("APOSENTADA"));
        assertEquals(TrancaStatus.EM_REPARO, TrancaStatus.valueOf("EM_REPARO"));
    }

    @Test
    void testToString() {
        assertEquals("LIVRE", TrancaStatus.LIVRE.toString());
        assertEquals("OCUPADA", TrancaStatus.OCUPADA.toString());
        assertEquals("NOVA", TrancaStatus.NOVA.toString());
        assertEquals("APOSENTADA", TrancaStatus.APOSENTADA.toString());
        assertEquals("EM_REPARO", TrancaStatus.EM_REPARO.toString());
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> TrancaStatus.valueOf("INEXISTENTE"));
    }
}