// scb-equipamentos/src/main/java/scb/microsservico/equipamentos/client/ExternalServiceClient.java
package scb.microsservico.equipamentos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import scb.microsservico.equipamentos.dto.Client.EmailRequestDTO;

@FeignClient(name = "scb-externo", url = "${scb-externo.url}")
public interface ExternoServiceClient {

    @PostMapping("/enviarEmail")
    void enviarEmail(@RequestBody EmailRequestDTO request);
}