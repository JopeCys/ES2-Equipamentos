package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaJaIntegradaException extends RuntimeException {
    public TrancaJaIntegradaException() {
        super("A tranca já está integrada ou está ocupada!");
    }
}