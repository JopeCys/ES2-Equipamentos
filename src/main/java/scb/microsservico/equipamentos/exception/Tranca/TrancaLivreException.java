package scb.microsservico.equipamentos.exception.Tranca;

public class TrancaLivreException extends RuntimeException {
    public TrancaLivreException() {
        super(("A tranca já está livre e não pode ser destrancada."));
    }
}
