package scb.microsservico.equipamentos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scb.microsservico.equipamentos.exception.GlobalExceptionHandler;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaJaIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaLivreException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNaoIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.exception.Client.FuncionarioNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFoundExceptions() {
        BicicletaNotFoundException ex = new BicicletaNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleNotFoundExceptions(ex));
    }

    @Test
    void handleBadRequestExceptions() {
        BicicletaOcupadaException ex = new BicicletaOcupadaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleTotemNotFoundException() {
        TotemNotFoundException ex = new TotemNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleNotFoundExceptions(ex));
    }

    @Test
    void handleTrancaNotFoundException() {
        TrancaNotFoundException ex = new TrancaNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleNotFoundExceptions(ex));
    }

    @Test
    void handleTrancaOcupadaException() {
        TrancaOcupadaException ex = new TrancaOcupadaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleTrancaLivreException() {
        TrancaLivreException ex = new TrancaLivreException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleTrancaJaIntegradaException() {
        TrancaJaIntegradaException ex = new TrancaJaIntegradaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleTrancaNaoIntegradaException() {
        TrancaNaoIntegradaException ex = new TrancaNaoIntegradaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Mensagem de teste");
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBadRequestExceptions(ex));
    }

    @Test
    void handleFuncionarioNotFoundException() {
        FuncionarioNotFoundException ex = new FuncionarioNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleNotFoundExceptions(ex));
    }
}