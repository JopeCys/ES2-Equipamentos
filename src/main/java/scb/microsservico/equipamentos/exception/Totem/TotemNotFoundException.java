package scb.microsservico.equipamentos.exception.Totem;

public class TotemNotFoundException extends RuntimeException {
    public TotemNotFoundException() {
        super("Error: 404. Totem não foi encontrado!");
    }
}