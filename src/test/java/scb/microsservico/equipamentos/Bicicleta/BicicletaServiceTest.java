package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.BicicletaService;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    @Mock
    private BicicletaRepository bicicletaRepository;
    @Mock
    private TrancaRepository trancaRepository;
    @Mock
    private TrancaService trancaService;
    @Mock
    private AluguelServiceClient aluguelServiceClient;
    @Mock
    private ExternoServiceClient externoServiceClient;

    @InjectMocks
    private BicicletaService bicicletaService;

    private Bicicleta bicicleta;
    private Tranca tranca;

    @BeforeEach
    void setUp() {
        bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(12345);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("10");
        bicicleta.setAno("2020");
        bicicleta.setStatus(BicicletaStatus.NOVA);

        tranca = new Tranca();
        tranca.setId(1L);
        tranca.setNumero(54321);
        tranca.setStatus(TrancaStatus.LIVRE);
    }

    @Test
    void criarBicicleta_DeveSalvarBicicletaComStatusNova() {
        BicicletaCreateDTO createDTO = new BicicletaCreateDTO();
        createDTO.setMarca("Caloi");
        createDTO.setModelo("10");
        createDTO.setAno("2020");

        when(bicicletaRepository.existsByNumero(anyInt())).thenReturn(false);
        when(bicicletaRepository.save(any(Bicicleta.class))).thenReturn(bicicleta);

        bicicletaService.criarBicicleta(createDTO);

        verify(bicicletaRepository, times(1)).save(any(Bicicleta.class));
    }

    @Test
    void buscarBicicletaPorId_QuandoEncontrada_DeveRetornarDTO() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        BicicletaResponseDTO result = bicicletaService.buscarBicicletaPorId(1L);

        assertNotNull(result);
        assertEquals(bicicleta.getId(), result.getId());
    }

    @Test
    void buscarBicicletaPorId_QuandoNaoEncontrada_DeveLancarExcecao() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BicicletaNotFoundException.class, () -> {
            bicicletaService.buscarBicicletaPorId(1L);
        });
    }

    @Test
    void buscarTodasBicicletas_DeveRetornarListaDeDTOs() {
        when(bicicletaRepository.findAll()).thenReturn(Collections.singletonList(bicicleta));

        List<BicicletaResponseDTO> result = bicicletaService.buscarTodasBicicletas();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void atualizarBicicleta_QuandoEncontrada_DeveAtualizarEretornarDTO() {
        BicicletaUpdateDTO updateDTO = new BicicletaUpdateDTO();
        updateDTO.setMarca("Monark");

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenReturn(bicicleta);

        BicicletaResponseDTO result = bicicletaService.atualizarBicicleta(1L, updateDTO);

        assertEquals("Monark", result.getMarca());
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void deletarBicicleta_ComStatusValido_DeveAlterarStatusParaAposentada() {
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        bicicletaService.deletarBicicleta(1L);

        assertEquals(BicicletaStatus.APOSENTADA, bicicleta.getStatus());
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void deletarBicicleta_ComStatusEmUso_DeveLancarExcecao() {
        bicicleta.setStatus(BicicletaStatus.EM_USO);
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        assertThrows(BicicletaOcupadaException.class, () -> {
            bicicletaService.deletarBicicleta(1L);
        });
    }

    @Test
    void alterarStatus_DeveAlterarEsalvar() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        bicicletaService.alterarStatus(1L, BicicletaStatus.EM_REPARO);

        assertEquals(BicicletaStatus.EM_REPARO, bicicleta.getStatus());
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void integrarBicicletaNaRede_ComDadosValidos_DeveIntegrarComSucesso() {
        IntegrarBicicletaDTO integrarDTO = new IntegrarBicicletaDTO();
        integrarDTO.setIdBicicleta(1L);
        integrarDTO.setIdTranca(1L);
        integrarDTO.setIdFuncionario(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn("teste@email.com");

        bicicletaService.integrarBicicletaNaRede(integrarDTO);

        verify(trancaService, times(1)).trancarTranca(eq(1L), any(TrancarRequestDTO.class));
        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(externoServiceClient, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void integrarBicicletaNaRede_ComBicicletaNaoNovaOuEmReparo_DeveLancarExcecao() {
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        IntegrarBicicletaDTO integrarDTO = new IntegrarBicicletaDTO();
        integrarDTO.setIdBicicleta(1L);
        integrarDTO.setIdTranca(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(integrarDTO);
        });
    }

    @Test
    void retirarBicicletaDaRede_ComDadosValidosParaReparo_DeveRetirarComSucesso() {
        bicicleta.setStatus(BicicletaStatus.EM_USO);
        tranca.setStatus(TrancaStatus.OCUPADA);
        RetirarBicicletaDTO retirarDTO = new RetirarBicicletaDTO();
        retirarDTO.setIdBicicleta(1L);
        retirarDTO.setIdTranca(1L);
        retirarDTO.setIdFuncionario(1L);
        retirarDTO.setAcao(AcaoRetirar.REPARO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn("teste@email.com");


        bicicletaService.retirarBicicletaDaRede(retirarDTO);

        verify(trancaService, times(1)).destrancarTranca(eq(1L), any(DestrancarRequestDTO.class));
        assertEquals(BicicletaStatus.EM_REPARO, bicicleta.getStatus());
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(externoServiceClient, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }
    
    @Test
    void retirarBicicletaDaRede_ComDadosValidosParaAposentadoria_DeveRetirarComSucesso() {
        bicicleta.setStatus(BicicletaStatus.EM_USO);
        tranca.setStatus(TrancaStatus.OCUPADA);
        RetirarBicicletaDTO retirarDTO = new RetirarBicicletaDTO();
        retirarDTO.setIdBicicleta(1L);
        retirarDTO.setIdTranca(1L);
        retirarDTO.setIdFuncionario(1L);
        retirarDTO.setAcao(AcaoRetirar.APOSENTADORIA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn("teste@email.com");


        bicicletaService.retirarBicicletaDaRede(retirarDTO);

        verify(trancaService, times(1)).destrancarTranca(eq(1L), any(DestrancarRequestDTO.class));
        assertEquals(BicicletaStatus.APOSENTADA, bicicleta.getStatus());
        verify(bicicletaRepository, times(1)).save(bicicleta);
        verify(externoServiceClient, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void retirarBicicletaDaRede_ComBicicletaDisponivel_DeveLancarExcecao() {
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        RetirarBicicletaDTO retirarDTO = new RetirarBicicletaDTO();
        retirarDTO.setIdBicicleta(1L);
        retirarDTO.setIdTranca(1L);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(retirarDTO);
        });
    }
}