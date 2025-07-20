package scb.microsservico.equipamentos.Tranca.exception;

import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class TrancaNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        TrancaNotFoundException exception = new TrancaNotFoundException();
        assertEquals("Tranca não foi encontrada!", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        TrancaNotFoundException exception = new TrancaNotFoundException();
        assertTrue(exception instanceof RuntimeException);
    }
}
