package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaNaoIntegradaException extends RuntimeException {
    public TrancaNaoIntegradaException() {
        super("A tranca não está integrada ou não corresponde ao totem!");
    }
}