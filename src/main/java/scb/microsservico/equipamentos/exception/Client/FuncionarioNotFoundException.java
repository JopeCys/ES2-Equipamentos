package scb.microsservico.equipamentos.exception.Client;

public class FuncionarioNotFoundException extends RuntimeException {
    public FuncionarioNotFoundException() {
        super("Funcionário não encontrado!");
    }
}
