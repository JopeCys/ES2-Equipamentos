package scb.microsservico.equipamentos.Totem.exception;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

class TotemNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        TotemNotFoundException exception = new TotemNotFoundException();
        assertEquals("Totem não foi encontrado!", exception.getMessage());
    }

    @Test
    void testIsRuntimeException() {
        TotemNotFoundException exception = new TotemNotFoundException();
        assertTrue(exception instanceof RuntimeException);
    }
}
