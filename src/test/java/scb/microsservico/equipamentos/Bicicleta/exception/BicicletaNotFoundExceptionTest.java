package scb.microsservico.equipamentos.Bicicleta.exception;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        BicicletaNotFoundException exception = new BicicletaNotFoundException();
        assertEquals("Bicicleta não foi encontrada!", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        BicicletaNotFoundException exception = new BicicletaNotFoundException();
        assertTrue(exception instanceof RuntimeException);
    }
}
