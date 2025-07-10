package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaOcupadaException extends RuntimeException {
    public TrancaOcupadaException() {
        super("Não é possível deletar uma tranca OCUPADA!");
    }
}