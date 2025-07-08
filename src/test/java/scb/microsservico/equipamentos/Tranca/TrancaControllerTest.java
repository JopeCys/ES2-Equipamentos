package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import scb.microsservico.equipamentos.controller.TrancaController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.IntegrarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.RetirarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
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
        assertEquals("Dados Cadastrados", response.getBody());
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
        assertEquals("Tranca Deletada", response.getBody());
        verify(trancaService, times(1)).deletarTranca(id);
    }

    @Test
    void testBuscarBicicletaNaTranca() {
        Long idTranca = 1L;
        BicicletaResponseDTO bicicleta = new BicicletaResponseDTO();
        when(trancaService.buscarBicicletaNaTranca(idTranca)).thenReturn(bicicleta);

        ResponseEntity<BicicletaResponseDTO> response = trancaController.buscarBicicletaNaTranca(idTranca);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(bicicleta, response.getBody());
        verify(trancaService, times(1)).buscarBicicletaNaTranca(idTranca);
    }

    @Test
    void testTrancarTranca() {
        Long idTranca = 1L;
        TrancarRequestDTO dto = new TrancarRequestDTO();
        doNothing().when(trancaService).trancarTranca(idTranca, dto);

        ResponseEntity<String> response = trancaController.trancarTranca(idTranca, dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Tranca trancada com sucesso", response.getBody());
        verify(trancaService, times(1)).trancarTranca(idTranca, dto);
    }

    @Test
    void testDestrancarTranca() {
        Long idTranca = 1L;
        DestrancarRequestDTO dto = new DestrancarRequestDTO();
        doNothing().when(trancaService).destrancarTranca(idTranca, dto);

        ResponseEntity<String> response = trancaController.destrancarTranca(idTranca, dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Tranca destrancada com sucesso", response.getBody());
        verify(trancaService, times(1)).destrancarTranca(idTranca, dto);
    }

    @Test
    void testAlterarStatusTranca() {
        Long idTranca = 1L;
        TrancaStatus acao = TrancaStatus.EM_REPARO;
        TrancaResponseDTO trancaResponse = new TrancaResponseDTO();

        doNothing().when(trancaService).alterarStatus(idTranca, acao);
        when(trancaService.buscarTrancaPorId(idTranca)).thenReturn(trancaResponse);

        ResponseEntity<TrancaResponseDTO> response = trancaController.alterarStatusTranca(idTranca, acao);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(trancaResponse, response.getBody());
        verify(trancaService, times(1)).alterarStatus(idTranca, acao);
        verify(trancaService, times(1)).buscarTrancaPorId(idTranca);
    }

    @Test
    void testIntegrarNaRede() {
        IntegrarTrancaDTO dto = new IntegrarTrancaDTO();
        doNothing().when(trancaService).integrarNaRede(dto);

        ResponseEntity<String> response = trancaController.integrarNaRede(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Tranca integrada com sucesso", response.getBody());
        verify(trancaService, times(1)).integrarNaRede(dto);
    }

    @Test
    void testRetirarDaRede() {
        RetirarTrancaDTO dto = new RetirarTrancaDTO();
        doNothing().when(trancaService).retirarDaRede(dto);

        ResponseEntity<String> response = trancaController.retirarDaRede(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Tranca retirada com sucesso", response.getBody());
        verify(trancaService, times(1)).retirarDaRede(dto);
    }
}
