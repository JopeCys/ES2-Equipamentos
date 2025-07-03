package scb.microsservico.equipamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;

@RestControllerAdvice // Aplica tratamento global de exceções para controllers REST
public class GlobalExceptionHandler {

    @ExceptionHandler(TrancaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 se tranca não encontrada
    @ResponseBody
    public String handleTrancaNotFoundException(TrancaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Retorna 422 para dados inválidos
    @ResponseBody
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return "Error: 422. Dados inválidos!";
    }

    @ExceptionHandler(TrancaOcupadaException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Retorna 409 se tranca estiver ocupada
    @ResponseBody
    public String handleTrancaOcupadaException(TrancaOcupadaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TotemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 se totem não encontrado
    @ResponseBody
    public String handleTotemNotFoundException(TotemNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BicicletaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 se bicicleta não encontrada
    @ResponseBody
    public String handleBicicletaNotFoundException(BicicletaNotFoundException ex) {
        return ex.getMessage();
    }
}