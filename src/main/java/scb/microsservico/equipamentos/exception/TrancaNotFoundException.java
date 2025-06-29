package scb.microsservico.equipamentos.exception;

public class TrancaNotFoundException extends RuntimeException {
    public TrancaNotFoundException() {
        super("Error: 404. Tranca não foi encontrada!");
    }
}