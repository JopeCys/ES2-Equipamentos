package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import scb.microsservico.equipamentos.dto.Tranca.*;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Tranca.*;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.EmailService;
import scb.microsservico.equipamentos.service.OperacaoService;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    private EmailService emailService;
    @Mock
    private OperacaoService operacaoService;
    
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
        totem.setLocalizacao("Rua Teste");
        totem.setTrancas(new ArrayList<>()); 
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
        tranca.setStatus(TrancaStatus.NOVA); // Status que permite integração
        IntegrarTrancaDTO request = new IntegrarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setIdFuncionario(1L);

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));

        trancaService.integrarNaRede(request);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertEquals(totem.getLocalizacao(), tranca.getLocalizacao());
        assertTrue(totem.getTrancas().contains(tranca));
        
        verify(totemRepository, times(1)).save(totem);
        verify(trancaRepository, times(1)).save(tranca);
        verify(operacaoService, times(1)).registrarOperacao(anyString(), anyString(), anyLong());
        verify(emailService, times(1)).enviarEmailNotificacao(anyLong(), anyString(), anyString());
    }
    
    @Test
    void integrarNaRede_QuandoJaIntegrada_DeveLancarExcecao() {
        tranca.setStatus(TrancaStatus.LIVRE); // Status que não permite integração
        IntegrarTrancaDTO request = new IntegrarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        
        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));

        assertThrows(TrancaJaIntegradaException.class, () -> trancaService.integrarNaRede(request));
    }


    @Test
    void retirarDaRede_deveRetirarTrancaDoTotem() {
        totem.getTrancas().add(tranca); // Adiciona a tranca ao totem para o teste
        RetirarTrancaDTO request = new RetirarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);
        request.setIdFuncionario(1L);

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));

        trancaService.retirarDaRede(request);

        assertEquals(TrancaStatus.EM_REPARO, tranca.getStatus());
        assertNull(tranca.getLocalizacao());
        assertFalse(totem.getTrancas().contains(tranca));
        
        verify(totemRepository, times(1)).save(totem);
        verify(trancaRepository, times(1)).save(tranca);
        verify(operacaoService, times(1)).registrarOperacao(anyString(), anyString(), anyLong());
        verify(emailService, times(1)).enviarEmailNotificacao(anyLong(), anyString(), anyString());
    }
    
    @Test
    void retirarDaRede_QuandoTrancaNaoEstaNoTotem_DeveLancarExcecao() {
        RetirarTrancaDTO request = new RetirarTrancaDTO();
        request.setIdTranca(tranca.getId());
        request.setIdTotem(totem.getId());
        request.setStatusAcaoReparador(AcaoRetirar.APOSENTADA);
        request.setIdFuncionario(1L);

        when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(totem.getId())).thenReturn(Optional.of(totem));
        
        assertThrows(TrancaNaoIntegradaException.class, () -> trancaService.retirarDaRede(request));
    }
}