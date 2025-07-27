package scb.microsservico.equipamentos;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Client.EmailRequestDTO;
import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;
import scb.microsservico.equipamentos.exception.Client.FuncionarioNotFoundException;
import scb.microsservico.equipamentos.service.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private ExternoServiceClient externoServiceClient;

    @Mock
    private AluguelServiceClient aluguelServiceClient;

    @InjectMocks
    private EmailService emailService;

    private FuncionarioEmailDTO funcionarioEmailDTO;

    @BeforeEach
    void setUp() {
        funcionarioEmailDTO = new FuncionarioEmailDTO();
        funcionarioEmailDTO.setEmail("funcionario@teste.com");
    }

    @Test
    void enviarEmailNotificacao_ComFuncionarioValido_DeveEnviarEmail() {
        // Arrange
        when(aluguelServiceClient.getEmailFuncionario(anyLong())).thenReturn(funcionarioEmailDTO);
        doNothing().when(externoServiceClient).enviarEmail(any(EmailRequestDTO.class));

        // Act
        emailService.enviarEmailNotificacao(1L, "Assunto Teste", "Mensagem Teste");

        // Assert
        ArgumentCaptor<EmailRequestDTO> captor = ArgumentCaptor.forClass(EmailRequestDTO.class);
        verify(externoServiceClient, times(1)).enviarEmail(captor.capture());
        
        EmailRequestDTO capturedRequest = captor.getValue();
        assertEquals("funcionario@teste.com", capturedRequest.getEmail());
        assertEquals("Assunto Teste", capturedRequest.getAssunto());
        assertEquals("Mensagem Teste", capturedRequest.getMensagem());
    }

    @Test
    void enviarEmailNotificacao_QuandoFuncionarioNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(aluguelServiceClient.getEmailFuncionario(anyLong())).thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(FuncionarioNotFoundException.class, () -> {
            emailService.enviarEmailNotificacao(1L, "Assunto", "Mensagem");
        });

        verify(externoServiceClient, never()).enviarEmail(any(EmailRequestDTO.class));
    }

    @Test
    void enviarEmailNotificacao_QuandoEmailDoFuncionarioEVazio_DeveLancarExcecao() {
        // Arrange
        funcionarioEmailDTO.setEmail("");
        when(aluguelServiceClient.getEmailFuncionario(anyLong())).thenReturn(funcionarioEmailDTO);

        // Act & Assert
        assertThrows(FuncionarioNotFoundException.class, () -> {
            emailService.enviarEmailNotificacao(1L, "Assunto", "Mensagem");
        });

        verify(externoServiceClient, never()).enviarEmail(any(EmailRequestDTO.class));
    }

    @Test
    void enviarEmailNotificacao_QuandoEmailDoFuncionarioENulo_DeveLancarExcecao() {
        // Arrange
        funcionarioEmailDTO.setEmail(null);
        when(aluguelServiceClient.getEmailFuncionario(anyLong())).thenReturn(funcionarioEmailDTO);

        // Act & Assert
        assertThrows(FuncionarioNotFoundException.class, () -> {
            emailService.enviarEmailNotificacao(1L, "Assunto", "Mensagem");
        });

        verify(externoServiceClient, never()).enviarEmail(any(EmailRequestDTO.class));
    }
}