package scb.microsservico.equipamentos.Tranca.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import scb.microsservico.equipamentos.exception.GlobalExceptionHandler;
import scb.microsservico.equipamentos.exception.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.TrancaOcupadaException;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleTrancaNotFoundException() {
        TrancaNotFoundException ex = new TrancaNotFoundException();
        String response = handler.handleTrancaNotFoundException(ex);
        assertEquals("Error: 404. Tranca não foi encontrada!", response);
    }

    @Test
    void testHandleTrancaOcupadaException() {
        TrancaOcupadaException ex = new TrancaOcupadaException();
        String response = handler.handleTrancaOcupadaException(ex);
        assertEquals("Error: 409. Não é possível deletar uma tranca OCUPADA!", response);
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("mensagem de erro");
        String response = handler.handleHttpMessageNotReadableException(ex);
        assertEquals("Error: 422. Dados inválidos!", response);
    }
}
