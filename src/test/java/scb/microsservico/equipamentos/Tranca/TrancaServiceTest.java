package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.IntegrarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.RetirarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaLivreException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TotemRepository totemRepository;

    @InjectMocks
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarTranca_deveSalvarTranca() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        Tranca tranca = new Tranca();

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            mapper.when(() -> TrancaMapper.toEntity(dto)).thenReturn(tranca);
            when(trancaRepository.save(tranca)).thenReturn(tranca);

            trancaService.criarTranca(dto);

            verify(trancaRepository, times(1)).save(tranca);
        }
    }

    @Test
    void buscarTrancaPorId_deveRetornarDTO() {
        Long id = 1L;
        Tranca tranca = new Tranca();
        TrancaResponseDTO dto = new TrancaResponseDTO();

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
            mapper.when(() -> TrancaMapper.toDTO(tranca)).thenReturn(dto);

            TrancaResponseDTO result = trancaService.buscarTrancaPorId(id);

            assertEquals(dto, result);
        }
    }

    @Test
    void buscarTrancaPorId_naoEncontrado_deveLancarExcecao() {
        Long id = 1L;
        when(trancaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TrancaNotFoundException.class, () -> trancaService.buscarTrancaPorId(id));
    }

    @Test
    void buscarTodasTrancas_deveRetornarListaDTO() {
        Tranca tranca1 = new Tranca();
        Tranca tranca2 = new Tranca();
        TrancaResponseDTO dto1 = new TrancaResponseDTO();
        TrancaResponseDTO dto2 = new TrancaResponseDTO();

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            when(trancaRepository.findAll()).thenReturn(Arrays.asList(tranca1, tranca2));
            mapper.when(() -> TrancaMapper.toDTO(tranca1)).thenReturn(dto1);
            mapper.when(() -> TrancaMapper.toDTO(tranca2)).thenReturn(dto2);

            List<TrancaResponseDTO> result = trancaService.buscarTodasTrancas();

            assertEquals(2, result.size());
            assertTrue(result.contains(dto1));
            assertTrue(result.contains(dto2));
        }
    }

    @Test
    void buscarTodasTrancas_listaVazia() {
        when(trancaRepository.findAll()).thenReturn(Collections.emptyList());
        List<TrancaResponseDTO> result = trancaService.buscarTodasTrancas();
        assertTrue(result.isEmpty());
    }

    @Test
    void atualizarTranca_deveAtualizarEDevolverDTO() {
        Long id = 1L;
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setLocalizacao("Nova Localizacao");
        dto.setAnoDeFabricacao("2020");
        dto.setModelo("Modelo X");

        Tranca tranca = new Tranca();
        TrancaResponseDTO responseDTO = new TrancaResponseDTO();

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            mapper.when(() -> TrancaMapper.toDTO(tranca)).thenReturn(responseDTO);

            TrancaResponseDTO result = trancaService.atualizarTranca(id, dto);

            assertEquals(responseDTO, result);
            assertEquals("Nova Localizacao", tranca.getLocalizacao());
            assertEquals("2020", tranca.getAnoDeFabricacao());
            assertEquals("Modelo X", tranca.getModelo());
        }
    }

    @Test
    void atualizarTranca_naoEncontrado_deveLancarExcecao() {
        Long id = 1L;
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        when(trancaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TrancaNotFoundException.class, () -> trancaService.atualizarTranca(id, dto));
    }

    @Test
    void deletarTranca_deveAposentarTranca() {
        Long id = 1L;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.deletarTranca(id);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void deletarTranca_ocupada_deveLancarExcecao() {
        Long id = 1L;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));

        assertThrows(TrancaOcupadaException.class, () -> trancaService.deletarTranca(id));
        verify(trancaRepository, never()).save(any());
    }

    @Test
    void deletarTranca_naoEncontrada_deveLancarExcecao() {
        Long id = 1L;
        when(trancaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TrancaNotFoundException.class, () -> trancaService.deletarTranca(id));
    }

    @Test
    void deletarTranca_aposentada_devePermitirAposentarNovamente() {
        Long id = 2L;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.APOSENTADA);

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        trancaService.deletarTranca(id);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    // Teste para garantir que atualizarTranca não altera outros campos
    @Test
    void atualizarTranca_naoAlteraCamposNaoAtualizados() {
        Long id = 3L;
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        dto.setLocalizacao("Loc nova");
        dto.setAnoDeFabricacao("2022");
        dto.setModelo("Modelo Y");

        Tranca tranca = new Tranca();
        tranca.setId(id);
        tranca.setStatus(TrancaStatus.LIVRE);
        tranca.setNumero(10);
        tranca.setBicicleta(99);

        TrancaResponseDTO responseDTO = new TrancaResponseDTO();

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            mapper.when(() -> TrancaMapper.toDTO(tranca)).thenReturn(responseDTO);

            trancaService.atualizarTranca(id, dto);

            assertEquals(10, tranca.getNumero());
            assertEquals(99, tranca.getBicicleta());
            assertEquals("Loc nova", tranca.getLocalizacao());
            assertEquals("2022", tranca.getAnoDeFabricacao());
            assertEquals("Modelo Y", tranca.getModelo());
        }
    }

     @Test
    void buscarBicicletaNaTranca_comSucesso_deveRetornarBicicletaDTO() {
        Long idTranca = 1L;
        int numeroBicicleta = 101;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(numeroBicicleta);

        Bicicleta bicicleta = new Bicicleta();
        BicicletaResponseDTO bicicletaDTO = new BicicletaResponseDTO();

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findByNumero(numeroBicicleta)).thenReturn(Optional.of(bicicleta));

        try (MockedStatic<BicicletaMapper> mapper = Mockito.mockStatic(BicicletaMapper.class)) {
            mapper.when(() -> BicicletaMapper.toDTO(bicicleta)).thenReturn(bicicletaDTO);
            BicicletaResponseDTO result = trancaService.buscarBicicletaNaTranca(idTranca);
            assertNotNull(result);
            assertEquals(bicicletaDTO, result);
        }
    }

    @Test
    void buscarBicicletaNaTranca_quandoTrancaNaoOcupada_deveLancarExcecao() {
        Long idTranca = 1L;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));

        assertThrows(BicicletaNotFoundException.class, () -> trancaService.buscarBicicletaNaTranca(idTranca));
    }

    @Test
    void buscarBicicletaNaTranca_quandoBicicletaNaoEncontrada_deveLancarExcecao() {
        Long idTranca = 1L;
        int numeroBicicleta = 101;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(numeroBicicleta);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findByNumero(numeroBicicleta)).thenReturn(Optional.empty());

        assertThrows(BicicletaNotFoundException.class, () -> trancaService.buscarBicicletaNaTranca(idTranca));
    }

    @Test
    void trancarTranca_comSucesso_deveMudarStatusEAssociarBicicleta() {
        Long idTranca = 1L;
        Long idBicicleta = 2L;
        
        TrancarRequestDTO dto = new TrancarRequestDTO();
        dto.setIdBicicleta(idBicicleta);

        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        bicicleta.setNumero(101);
        bicicleta.setStatus(BicicletaStatus.EM_USO);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(idBicicleta)).thenReturn(Optional.of(bicicleta));

        trancaService.trancarTranca(idTranca, dto);

        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        assertEquals(bicicleta.getNumero(), tranca.getBicicleta());
        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void trancarTranca_jaOcupada_deveLancarExcecao() {
        Long idTranca = 1L;
        TrancarRequestDTO dto = new TrancarRequestDTO();
        dto.setIdBicicleta(2L);
        
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));

        assertThrows(TrancaOcupadaException.class, () -> trancaService.trancarTranca(idTranca, dto));
    }

    @Test
    void destrancarTranca_comSucesso_deveMudarStatusEDesassociarBicicleta() {
        Long idTranca = 1L;
        Long idBicicleta = 2L;
        
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        dto.setIdBicicleta(idBicicleta);

        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.OCUPADA);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(idBicicleta);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(idBicicleta)).thenReturn(Optional.of(bicicleta));

        trancaService.destrancarTranca(idTranca, dto);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertNull(tranca.getBicicleta());
        assertEquals(BicicletaStatus.EM_USO, bicicleta.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
        verify(bicicletaRepository, times(1)).save(bicicleta);
    }

    @Test
    void destrancarTranca_jaLivre_deveLancarExcecao() {
        Long idTranca = 1L;
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        dto.setIdBicicleta(2L);
        
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));

        assertThrows(TrancaLivreException.class, () -> trancaService.destrancarTranca(idTranca, dto));
    }

    @Test
    void alterarStatus_comSucesso_deveSalvarNovoStatus() {
        Long idTranca = 1L;
        TrancaStatus novoStatus = TrancaStatus.EM_REPARO;
        Tranca tranca = new Tranca();
        tranca.setStatus(TrancaStatus.LIVRE);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));

        trancaService.alterarStatus(idTranca, novoStatus);

        assertEquals(novoStatus, tranca.getStatus());
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void alterarStatus_trancaNaoEncontrada_deveLancarExcecao() {
        Long idTranca = 1L;
        when(trancaRepository.findById(idTranca)).thenReturn(Optional.empty());

        assertThrows(TrancaNotFoundException.class, () -> trancaService.alterarStatus(idTranca, TrancaStatus.LIVRE));
    }

    @Test
    void integrarNaRede_comSucesso_deveMudarStatusEAdicionarAoTotem() {
        Long idTranca = 1L;
        Long idTotem = 10L;
        
        IntegrarTrancaDTO dto = new IntegrarTrancaDTO();
        dto.setIdTranca(idTranca);
        dto.setIdTotem(idTotem);

        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(TrancaStatus.NOVA);

        Totem totem = new Totem();
        totem.setId(idTotem);
        totem.setTrancas(new ArrayList<>());

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));
        when(trancaRepository.save(any(Tranca.class))).thenReturn(tranca);

        trancaService.integrarNaRede(dto);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertTrue(totem.getTrancas().contains(tranca));
        verify(totemRepository, times(1)).save(totem);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void integrarNaRede_comStatusInvalido_deveLancarExcecao() {
        Long idTranca = 1L;
        Long idTotem = 10L;

        IntegrarTrancaDTO dto = new IntegrarTrancaDTO();
        dto.setIdTranca(idTranca);
        dto.setIdTotem(idTotem);

        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(TrancaStatus.OCUPADA); // Status inválido para integração

        Totem totem = new Totem();
        totem.setId(idTotem);

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));

        assertThrows(IllegalStateException.class, () -> trancaService.integrarNaRede(dto));
    }

    @Test
    void retirarDaRede_comSucesso_deveMudarStatusERemoverDoTotem() {
        Long idTranca = 1L;
        Long idTotem = 10L;

        RetirarTrancaDTO dto = new RetirarTrancaDTO();
        dto.setIdTranca(idTranca);
        dto.setIdTotem(idTotem);

        Tranca tranca = new Tranca();
        tranca.setId(idTranca);
        tranca.setStatus(TrancaStatus.LIVRE);

        Totem totem = new Totem();
        totem.setId(idTotem);
        totem.setTrancas(new ArrayList<>(List.of(tranca)));

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));
        when(trancaRepository.save(any(Tranca.class))).thenReturn(tranca);

        trancaService.retirarDaRede(dto);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        assertFalse(totem.getTrancas().contains(tranca));
        verify(totemRepository, times(1)).save(totem);
        verify(trancaRepository, times(1)).save(tranca);
    }

    @Test
    void retirarDaRede_quandoTrancaNaoEstaNoTotem_deveLancarExcecao() {
        Long idTranca = 1L;
        Long idTotem = 10L;
        
        RetirarTrancaDTO dto = new RetirarTrancaDTO();
        dto.setIdTranca(idTranca);
        dto.setIdTotem(idTotem);

        Tranca tranca = new Tranca(); // Tranca a ser removida
        tranca.setId(idTranca);

        Totem totem = new Totem(); // Totem não contém a tranca
        totem.setId(idTotem);
        totem.setTrancas(new ArrayList<>());

        when(trancaRepository.findById(idTranca)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));

        assertThrows(IllegalStateException.class, () -> trancaService.retirarDaRede(dto));
    }
}