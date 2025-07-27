package scb.microsservico.equipamentos.service;

import org.springframework.stereotype.Service;

import feign.FeignException;

import lombok.RequiredArgsConstructor;

import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Client.EmailRequestDTO;
import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;
import scb.microsservico.equipamentos.exception.Client.FuncionarioNotFoundException;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class EmailService {

    private final ExternoServiceClient externoServiceClient;
    private final AluguelServiceClient aluguelServiceClient;

    public void enviarEmailNotificacao(Long idFuncionario, String assunto, String mensagem) {
        FuncionarioEmailDTO emailDTO;
        try {
            emailDTO = aluguelServiceClient.getEmailFuncionario(idFuncionario);
        } catch (FeignException.NotFound e) {
            throw new FuncionarioNotFoundException();
        }
        if (emailDTO == null || emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
            throw new FuncionarioNotFoundException();
        }
        
        EmailRequestDTO emailRequest = new EmailRequestDTO();
        emailRequest.setEmail(emailDTO.getEmail());
        emailRequest.setAssunto(assunto);
        emailRequest.setMensagem(mensagem);
        externoServiceClient.enviarEmail(emailRequest);
    }
}