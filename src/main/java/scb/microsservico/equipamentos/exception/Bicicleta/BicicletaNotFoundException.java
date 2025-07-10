package scb.microsservico.equipamentos.exception.Bicicleta;

public class BicicletaNotFoundException extends RuntimeException {
    public BicicletaNotFoundException() {
        super("Bicicleta não foi encontrada!");
    }
}