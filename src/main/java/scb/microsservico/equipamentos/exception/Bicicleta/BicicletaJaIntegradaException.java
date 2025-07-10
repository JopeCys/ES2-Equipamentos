package scb.microsservico.equipamentos.exception.Bicicleta;

public class BicicletaJaIntegradaException extends RuntimeException {
    public BicicletaJaIntegradaException() {
        super("A bicicleta já está integrada ou a tranca está ocupada!");
    }
}