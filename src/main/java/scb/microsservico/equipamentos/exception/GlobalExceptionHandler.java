package scb.microsservico.equipamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaJaIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaLivreException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNaoIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manipuladores para exceções de Bicicleta
    @ExceptionHandler(BicicletaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    @ResponseBody
    public String handleBicicletaNotFoundException(BicicletaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BicicletaOcupadaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleBicicletaOcupadaException(BicicletaOcupadaException ex) {
        return ex.getMessage();
    }

    // Manipulador para exceções de Totem
    @ExceptionHandler(TotemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    @ResponseBody
    public String handleTotemNotFoundException(TotemNotFoundException ex) {
        return ex.getMessage();
    }

    // Manipuladores para exceções de Tranca
    @ExceptionHandler(TrancaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    @ResponseBody
    public String handleTrancaNotFoundException(TrancaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TrancaOcupadaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleTrancaOcupadaException(TrancaOcupadaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TrancaLivreException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleTrancaLivreException(TrancaLivreException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TrancaJaIntegradaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleTrancaJaIntegradaException(TrancaJaIntegradaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TrancaNaoIntegradaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleTrancaNaoIntegradaException(TrancaNaoIntegradaException ex) {
        return ex.getMessage();
    }

    // Manipulador para exceções de estado ilegal
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ResponseBody
    public String handleIllegalStateException(IllegalStateException ex) {
        return ex.getMessage();
    }
}