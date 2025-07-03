package scb.microsservico.equipamentos.exception.Bicicleta;

public class BicicletaNotFoundException extends RuntimeException {
    public BicicletaNotFoundException() {
        super("Error: 404. Bicicleta não foi encontrada!");
    }
}