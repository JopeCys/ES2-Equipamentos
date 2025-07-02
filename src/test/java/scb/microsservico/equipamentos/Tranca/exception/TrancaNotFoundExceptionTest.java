package scb.microsservico.equipamentos.Tranca.exception;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.exception.TrancaNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

class TrancaNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        TrancaNotFoundException exception = new TrancaNotFoundException();
        assertEquals("Error: 404. Tranca não foi encontrada!", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        TrancaNotFoundException exception = new TrancaNotFoundException();
        assertTrue(exception instanceof RuntimeException);
    }
}
