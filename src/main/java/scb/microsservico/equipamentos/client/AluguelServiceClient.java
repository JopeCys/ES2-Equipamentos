// scb-equipamentos/src/main/java/scb/microsservico/equipamentos/client/RentalServiceClient.java
package scb.microsservico.equipamentos.client;

import org.springframework.stereotype.Component;

// Esta é uma interface de placeholder. Em uma aplicação real,
// seria uma interface FeignClient ou um componente que usa RestTemplate.
@Component
public class AluguelServiceClient {

    public String getEmailFuncionario(Long idFuncionario) {
        // Lógica para buscar o e-mail do funcionário no microsserviço aluguel
        // Em um ambiente real:
        // Ex: return feignClient.getEmployeeDetails(employeeId).getEmail();
        System.out.println("DEBUG: Buscando e-mail para o funcionário com ID: " + idFuncionario);
        return "funcionario" + idFuncionario + "@email.com"; // Exemplo de retorno
    }
}