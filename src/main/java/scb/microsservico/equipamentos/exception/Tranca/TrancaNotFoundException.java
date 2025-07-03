package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaNotFoundException extends RuntimeException {
    public TrancaNotFoundException() {
        super("Error: 404. Tranca não foi encontrada!");
    }
}