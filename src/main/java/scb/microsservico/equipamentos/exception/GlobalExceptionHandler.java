package scb.microsservico.equipamentos.exception;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.exception.Client.FuncionarioNotFoundException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaJaIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaLivreException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNaoIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Manipulador para todas as exceções que devem retornar 404 Not Found.
     * Retorna a mensagem da exceção como uma String no corpo da resposta.
     */
    @ExceptionHandler({
        BicicletaNotFoundException.class,
        TotemNotFoundException.class,
        TrancaNotFoundException.class,
        FuncionarioNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ResponseBody
    public String handleNotFoundExceptions(RuntimeException ex) {
        return ex.getMessage();
    }

    /**
     * Manipulador para todas as exceções que devem retornar 400 Bad Request.
     * Retorna a mensagem da exceção como uma String no corpo da resposta.
     */
    @ExceptionHandler({
        BicicletaOcupadaException.class,
        TrancaOcupadaException.class,
        TrancaLivreException.class,
        TrancaJaIntegradaException.class,
        TrancaNaoIntegradaException.class,
        IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ResponseBody
    public String handleBadRequestExceptions(RuntimeException ex) {
        return ex.getMessage();
    }
}
