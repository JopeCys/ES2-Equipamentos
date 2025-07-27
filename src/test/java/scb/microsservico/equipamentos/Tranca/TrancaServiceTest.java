package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Client.EmailRequestDTO;
import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;
import scb.microsservico.equipamentos.dto.Tranca.*;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Tranca.*;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;
    @Mock
    private BicicletaRepository bicicletaRepository;
    @Mock
    private TotemRepository totemRepository;
    @Mock
    private ExternoServiceClient externoServiceClient;
    @Mock
    private AluguelServiceClient aluguelServiceClient;
    @Mock
    private RegistroOperacaoRepository registroOperacaoRepository;

    @InjectMocks
    private TrancaService trancaService;

    private Tranca tranca;
    private Bicicleta bicicleta;
    private Totem totem;

    @BeforeEach
    void setUp() {
        tranca = new Tranca();
        tranca.setId(1L);
        tranca.setNumero(101);
        tranca.setStatus(TrancaStatus.LIVRE);

        bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(202);

        totem = new Totem();
        totem.setId(1L);
    }

    @Test
    void criarTranca_deveSalvarTrancaComStatusNova() {
        TrancaCreateDTO createDTO = new TrancaCreateDTO();
        trancaService.criarTranca(createDTO);
        verify(trancaRepository, times(1)).save(any(Tranca.class));
    }

    @Test
    void buscarTrancaPorId_quandoEncontrada_deveRetornarDTO() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        TrancaResponseDTO result = trancaService.buscarTrancaPorId(1L);
        assertNotNull(result);
        assertEquals(tranca.getId(), result.getId());
    }

    @Test
    void buscarTrancaPorId_quandoNaoEncontrada_deveLancarExcecao() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TrancaNotFoundException.class, () -> trancaService.buscarTrancaPorId(1L));
    }

    @Test
    void atualizarTranca_deveAtualizarDados() {
        TrancaUpdateDTO updateDTO = new TrancaUpdateDTO();
        updateDTO.setModelo("Novo Modelo");
        updateDTO.setAnoDeFabricacao("2024");
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        trancaService.atualizarTranca(1L, updateDTO);
        assertEquals("Novo Modelo", tranca.getModelo());
        assertEquals("2024", tranca.getAnoDeFabricacao());
    }

    @Test
    void deletarTranca_quandoNaoOcupada_deveAposentar() {
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        trancaService.deletarTranca(1L);
        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
    }

    @Test
    void deletarTranca_quandoOcupada_deveLancarExcecao() {
        tranca.setStatus(TrancaStatus.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        assertThrows(TrancaOcupadaException.class, () -> trancaService.deletarTranca(1L));
    }

    @Test
    void trancarTranca_deveAssociarBicicletaETrancar() {
        tranca.setStatus(TrancaStatus.LIVRE);
        TrancarRequestDTO request = new TrancarRequestDTO();
        request.setBicicleta(bicicleta.getId());

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(bicicleta.getId())).thenReturn(Optional.of(bicicleta));

        trancaService.trancarTranca(1L, request);

        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        assertEquals(bicicleta.getNumero(), tranca.getBicicleta());
        assertEquals("DISPONIVEL", bicicleta.getStatus().name());
    }

    @Test
    void destrancarTranca_deveDesassociarBicicletaEDestrancar() {
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(bicicleta.getNumero());
        DestrancarRequestDTO request = new DestrancarRequestDTO();
        request.setBicicleta(bicicleta.getId());

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(bicicleta.getId())).thenReturn(Optional.of(bicicleta));

        trancaService.destrancarTranca(1L, request);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertNull(tranca.getBicicleta());
        assertEquals("EM_USO", bicicleta.getStatus().name());
        assertNull(bicicleta.getLocalizacao());
    }

    @Test
    void integrarNaRede_deveIntegrarTrancaAoTotem() {
        tranca.setStatus(TrancaStatus.NOVA);
        IntegrarTrancaDTO request = new IntegrarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setIdFuncionario(1L);

        FuncionarioEmailDTO funcionarioEmailDTO = new FuncionarioEmailDTO();
        funcionarioEmailDTO.setEmail("reparador@email.com");

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn(funcionarioEmailDTO);

        trancaService.integrarNaRede(request);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        verify(externoServiceClient, times(1)).enviarEmail(any(EmailRequestDTO.class));
    }

    @Test
    void retirarDaRede_deveRetirarTrancaDoTotem() {
        totem.setTrancas(new java.util.ArrayList<>(java.util.List.of(tranca)));
        RetirarTrancaDTO request = new RetirarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);
        request.setIdFuncionario(1L);

        FuncionarioEmailDTO funcionarioEmailDTO = new FuncionarioEmailDTO();
        funcionarioEmailDTO.setEmail("reparador@email.com");

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn(funcionarioEmailDTO);

        trancaService.retirarDaRede(request);

        assertEquals(TrancaStatus.EM_REPARO, tranca.getStatus());
        verify(externoServiceClient, times(1)).enviarEmail(any(EmailRequestDTO.class));
    }

    @Test
    void integrarNaRede_QuandoFalhaEnvioEmail_NaoLancaExcecao() {
        tranca.setStatus(TrancaStatus.NOVA);
        IntegrarTrancaDTO request = new IntegrarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setIdFuncionario(1L);

        FuncionarioEmailDTO funcionarioEmailDTO = new FuncionarioEmailDTO();
        funcionarioEmailDTO.setEmail("reparador@email.com");

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));
        when(aluguelServiceClient.getEmailFuncionario(1L)).thenReturn(funcionarioEmailDTO);
        doThrow(new RuntimeException("Falha no envio")).when(externoServiceClient).enviarEmail(any(EmailRequestDTO.class));

        assertDoesNotThrow(() -> trancaService.integrarNaRede(request));
        verify(externoServiceClient, times(1)).enviarEmail(any(EmailRequestDTO.class));
    }
}