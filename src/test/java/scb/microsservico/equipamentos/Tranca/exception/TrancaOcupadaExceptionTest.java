package scb.microsservico.equipamentos.Tranca.exception;

import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;

import static org.junit.jupiter.api.Assertions.*;

class TrancaOcupadaExceptionTest {

    @Test
    void testExceptionMessage() {
        TrancaOcupadaException exception = new TrancaOcupadaException();
        assertEquals("Não é possível deletar uma tranca OCUPADA!", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        TrancaOcupadaException exception = new TrancaOcupadaException();
        assertTrue(exception instanceof RuntimeException);
    }
}
