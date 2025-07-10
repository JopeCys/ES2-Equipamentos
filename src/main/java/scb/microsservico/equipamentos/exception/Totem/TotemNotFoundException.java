package scb.microsservico.equipamentos.exception.Totem;

public class TotemNotFoundException extends RuntimeException {
    public TotemNotFoundException() {
        super("Totem não foi encontrado!");
    }
}