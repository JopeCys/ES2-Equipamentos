package scb.microsservico.equipamentos.exception;

public class TrancaOcupadaException extends RuntimeException {
    public TrancaOcupadaException() {
        super("Error: 409. Não é possível deletar uma tranca OCUPADA!");
    }
}