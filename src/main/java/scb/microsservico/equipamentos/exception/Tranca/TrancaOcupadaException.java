package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaOcupadaException extends RuntimeException {
    public TrancaOcupadaException() {
        super("Error: 409. Não é possível deletar uma tranca OCUPADA!");
    }
}