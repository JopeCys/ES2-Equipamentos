package scb.microsservico.equipamentos.exception.Bicicleta;

public class BicicletaNaoIntegradaException extends RuntimeException {
    public BicicletaNaoIntegradaException() {
        super("A bicicleta não está integrada ou não corresponde à tranca!");
    }
}