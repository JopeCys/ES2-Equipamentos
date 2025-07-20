// scb-equipamentos/src/main/java/scb/microsservico/equipamentos/client/RentalServiceClient.java
package scb.microsservico.equipamentos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;

@FeignClient(name = "scb-aluguel", url = "${scb-aluguel.url}")
public interface AluguelServiceClient {

    @GetMapping("/funcionario/{idFuncionario}")
    FuncionarioEmailDTO getEmailFuncionario(@PathVariable("idFuncionario") Long idFuncionario);
}