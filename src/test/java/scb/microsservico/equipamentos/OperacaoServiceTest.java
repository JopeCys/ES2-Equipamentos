package scb.microsservico.equipamentos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scb.microsservico.equipamentos.model.RegistroOperacao;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;
import scb.microsservico.equipamentos.service.OperacaoService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacaoServiceTest {

    @Mock
    private RegistroOperacaoRepository registroOperacaoRepository;

    @InjectMocks
    private OperacaoService operacaoService;

    @Test
    void registrarOperacao_DeveCriarESalvarRegistro() {
        // Arrange
        String tipo = "INTEGRACAO";
        String descricao = "Teste de integração";
        Long idFuncionario = 123L;

        when(registroOperacaoRepository.save(any(RegistroOperacao.class))).thenReturn(new RegistroOperacao());

        // Act
        operacaoService.registrarOperacao(tipo, descricao, idFuncionario);

        // Assert
        ArgumentCaptor<RegistroOperacao> captor = ArgumentCaptor.forClass(RegistroOperacao.class);
        verify(registroOperacaoRepository, times(1)).save(captor.capture());

        RegistroOperacao registroCapturado = captor.getValue();
        assertEquals(tipo, registroCapturado.getTipo());
        assertEquals(descricao, registroCapturado.getDescricao());
        assertEquals(idFuncionario, registroCapturado.getIdFuncionario());
        assertNotNull(registroCapturado.getDataHora());
    }
}