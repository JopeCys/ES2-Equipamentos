package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaNotFoundException extends RuntimeException {
    public TrancaNotFoundException() {
        super("Tranca não foi encontrada!");
    }
}