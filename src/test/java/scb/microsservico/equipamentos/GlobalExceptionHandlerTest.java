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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBicicletaNotFoundException() {
        BicicletaNotFoundException ex = new BicicletaNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBicicletaNotFoundException(ex));
    }

    @Test
    void handleBicicletaOcupadaException() {
        BicicletaOcupadaException ex = new BicicletaOcupadaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleBicicletaOcupadaException(ex));
    }

    @Test
    void handleTotemNotFoundException() {
        TotemNotFoundException ex = new TotemNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTotemNotFoundException(ex));
    }

    @Test
    void handleTrancaNotFoundException() {
        TrancaNotFoundException ex = new TrancaNotFoundException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTrancaNotFoundException(ex));
    }

    @Test
    void handleTrancaOcupadaException() {
        TrancaOcupadaException ex = new TrancaOcupadaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTrancaOcupadaException(ex));
    }

    @Test
    void handleTrancaLivreException() {
        TrancaLivreException ex = new TrancaLivreException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTrancaLivreException(ex));
    }

    @Test
    void handleTrancaJaIntegradaException() {
        TrancaJaIntegradaException ex = new TrancaJaIntegradaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTrancaJaIntegradaException(ex));
    }

    @Test
    void handleTrancaNaoIntegradaException() {
        TrancaNaoIntegradaException ex = new TrancaNaoIntegradaException();
        assertEquals(ex.getMessage(), globalExceptionHandler.handleTrancaNaoIntegradaException(ex));
    }

    @Test
    void handleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Mensagem de teste");
        assertEquals(ex.getMessage(), globalExceptionHandler.handleIllegalStateException(ex));
    }
}