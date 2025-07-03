package scb.microsservico.equipamentos.Bicicleta.exception;

import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;

import static org.junit.jupiter.api.Assertions.*;

public class BicicletaOcupadaExceptionTest {

    @Test
    void testExceptionMessage() {
        BicicletaOcupadaException exception = new BicicletaOcupadaException();
        assertEquals("Error: 409. Não é possível deletar uma bicicleta EM USO!", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        BicicletaOcupadaException exception = new BicicletaOcupadaException();
        assertTrue(exception instanceof RuntimeException);
    }
}
