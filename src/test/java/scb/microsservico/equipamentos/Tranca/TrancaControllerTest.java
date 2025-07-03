package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import scb.microsservico.equipamentos.controller.TrancaController;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.service.TrancaService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TrancaControllerTest {

    private TrancaService trancaService;
    private TrancaController trancaController;

    @BeforeEach
    void setUp() {
        trancaService = mock(TrancaService.class);
        trancaController = new TrancaController(trancaService);
    }

    @Test
    void testCriarTranca() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        doNothing().when(trancaService).criarTranca(dto);

        ResponseEntity<String> response = trancaController.criarTranca(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Código 202: Dados Cadastrados", response.getBody());
        verify(trancaService, times(1)).criarTranca(dto);
    }

    @Test
    void testBuscarTrancaPorId() {
        Long id = 1L;
        TrancaResponseDTO mockResponse = new TrancaResponseDTO();
        when(trancaService.buscarTrancaPorId(id)).thenReturn(mockResponse);

        ResponseEntity<TrancaResponseDTO> response = trancaController.buscarTrancaPorId(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockResponse, response.getBody());
        verify(trancaService, times(1)).buscarTrancaPorId(id);
    }

    @Test
    void testBuscarTodasTrancas() {
        List<TrancaResponseDTO> mockList = Arrays.asList(new TrancaResponseDTO(), new TrancaResponseDTO());
        when(trancaService.buscarTodasTrancas()).thenReturn(mockList);

        ResponseEntity<List<TrancaResponseDTO>> response = trancaController.buscarTodasTrancas();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockList, response.getBody());
        verify(trancaService, times(1)).buscarTodasTrancas();
    }

    @Test
    void testAtualizarTranca() {
        Long id = 2L;
        TrancaUpdateDTO dto = new TrancaUpdateDTO();
        TrancaResponseDTO updated = new TrancaResponseDTO();
        when(trancaService.atualizarTranca(id, dto)).thenReturn(updated);

        ResponseEntity<TrancaResponseDTO> response = trancaController.atualizarTranca(id, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updated, response.getBody());
        verify(trancaService, times(1)).atualizarTranca(id, dto);
    }

    @Test
    void testDeletarTranca() {
        Long id = 3L;
        doNothing().when(trancaService).deletarTranca(id);

        ResponseEntity<String> response = trancaController.deletarTranca(id);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Código 202: Tranca Deletada", response.getBody());
        verify(trancaService, times(1)).deletarTranca(id);
    }
}
