package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import scb.microsservico.equipamentos.dto.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.TrancaUpdateDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.TrancaOcupadaException;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.TrancaService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;

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
        tranca.setBicicleta(99L);

        TrancaResponseDTO responseDTO = new TrancaResponseDTO();

        when(trancaRepository.findById(id)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(tranca)).thenReturn(tranca);

        try (MockedStatic<TrancaMapper> mapper = Mockito.mockStatic(TrancaMapper.class)) {
            mapper.when(() -> TrancaMapper.toDTO(tranca)).thenReturn(responseDTO);

            trancaService.atualizarTranca(id, dto);

            assertEquals(10, tranca.getNumero());
            assertEquals(99L, tranca.getBicicleta());
            assertEquals("Loc nova", tranca.getLocalizacao());
            assertEquals("2022", tranca.getAnoDeFabricacao());
            assertEquals("Modelo Y", tranca.getModelo());
        }
    }
}