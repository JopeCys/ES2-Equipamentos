// scb-equipamentos/src/main/java/scb/microsservico/equipamentos/client/ExternalServiceClient.java
package scb.microsservico.equipamentos.client;

import org.springframework.stereotype.Component;

// Esta é uma interface de placeholder. Em uma aplicação real,
// seria uma interface FeignClient ou um componente que usa RestTemplate.
@Component
public class ExternoServiceClient {

    public void enviarEmail(String emailIntegracao, String assunto, String corpo) {
        // Lógica para enviar e-mail para o microsserviço externo
        // Em um ambiente real:
        // Ex: feignClient.sendEmail(emailRequest);
        System.out.println("DEBUG: Enviando e-mail de integração para: " + emailIntegracao);
        System.out.println("DEBUG: Assunto: " + assunto);
        System.out.println("DEBUG: Corpo: " + corpo);
    }
}