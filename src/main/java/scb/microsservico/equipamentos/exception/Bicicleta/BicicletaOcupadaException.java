package scb.microsservico.equipamentos.exception.Bicicleta;

public class BicicletaOcupadaException extends RuntimeException {
    public BicicletaOcupadaException() {
        super("Error: 409. Não é possível deletar uma bicicleta EM USO!");
    }
}