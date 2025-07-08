package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import scb.microsservico.equipamentos.controller.BicicletaController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.service.BicicletaService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BicicletaControllerTest {

    @Mock
    private BicicletaService bicicletaService;

    @InjectMocks
    private BicicletaController bicicletaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarBicicleta() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        doNothing().when(bicicletaService).criarBicicleta(dto);

        ResponseEntity<String> response = bicicletaController.criarBicicleta(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Bicicleta Cadastrada", response.getBody());
        verify(bicicletaService, times(1)).criarBicicleta(dto);
    }

    @Test
    void testBuscarBicicletaPorId() {
        Long id = 1L;
        BicicletaResponseDTO bicicleta = new BicicletaResponseDTO();
        when(bicicletaService.buscarBicicletaPorId(id)).thenReturn(bicicleta);

        ResponseEntity<BicicletaResponseDTO> response = bicicletaController.buscarBicicletaPorId(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(bicicleta, response.getBody());
        verify(bicicletaService, times(1)).buscarBicicletaPorId(id);
    }

    @Test
    void testBuscarTodasBicicletas() {
        BicicletaResponseDTO b1 = new BicicletaResponseDTO();
        BicicletaResponseDTO b2 = new BicicletaResponseDTO();
        List<BicicletaResponseDTO> lista = Arrays.asList(b1, b2);
        when(bicicletaService.buscarTodasBicicletas()).thenReturn(lista);

        ResponseEntity<List<BicicletaResponseDTO>> response = bicicletaController.buscarTodasBicicletas();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(lista, response.getBody());
        verify(bicicletaService, times(1)).buscarTodasBicicletas();
    }

    @Test
    void testAtualizarBicicleta() {
        Long id = 1L;
        BicicletaUpdateDTO dto = new BicicletaUpdateDTO();
        BicicletaResponseDTO updated = new BicicletaResponseDTO();
        when(bicicletaService.atualizarBicicleta(id, dto)).thenReturn(updated);

        ResponseEntity<BicicletaResponseDTO> response = bicicletaController.atualizarBicicleta(id, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updated, response.getBody());
        verify(bicicletaService, times(1)).atualizarBicicleta(id, dto);
    }

    @Test
    void testDeletarBicicleta() {
        Long id = 1L;
        doNothing().when(bicicletaService).deletarBicicleta(id);

        ResponseEntity<String> response = bicicletaController.deletarBicicleta(id);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Bicicleta Deletada", response.getBody());
        verify(bicicletaService, times(1)).deletarBicicleta(id);
    }
@Test
    void testAlterarStatusBicicleta() {
        Long idBicicleta = 1L;
        BicicletaStatus novoStatus = BicicletaStatus.EM_REPARO;
        
        BicicletaResponseDTO bicicletaComStatusAlterado = new BicicletaResponseDTO();
        bicicletaComStatusAlterado.setId(idBicicleta);
        bicicletaComStatusAlterado.setStatus(novoStatus); 

        doNothing().when(bicicletaService).alterarStatus(idBicicleta, novoStatus);
        when(bicicletaService.buscarBicicletaPorId(idBicicleta)).thenReturn(bicicletaComStatusAlterado);

        ResponseEntity<BicicletaResponseDTO> response = bicicletaController.alterarStatusBicicleta(idBicicleta, novoStatus);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(novoStatus, response.getBody().getStatus());
        verify(bicicletaService, times(1)).alterarStatus(idBicicleta, novoStatus);
        verify(bicicletaService, times(1)).buscarBicicletaPorId(idBicicleta);
    }

    @Test
    void testIntegrarBicicletaNaRede() {
        // FIX: Instanciar o DTO e preencher com os setters, usando os tipos corretos.
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(10L);
        dto.setIdFuncionario(100L); // ID do funcionário em vez de uma string de status

        doNothing().when(bicicletaService).integrarBicicletaNaRede(dto);

        ResponseEntity<String> response = bicicletaController.integrarBicicletaNaRede(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Bicicleta Integrada", response.getBody());
        verify(bicicletaService, times(1)).integrarBicicletaNaRede(dto);
    }

    @Test
    void testRetirarBicicletaDaRede() {
        // FIX: Instanciar o DTO e preencher com os setters, usando os tipos corretos.
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(10L);
        dto.setIdFuncionario(100L);

        doNothing().when(bicicletaService).retirarBicicletaDaRede(dto);

        ResponseEntity<String> response = bicicletaController.retirarBicicletaDaRede(dto);

        assertEquals(202, response.getStatusCode().value());
        assertEquals("Bicicleta Retirada", response.getBody());
        verify(bicicletaService, times(1)).retirarBicicletaDaRede(dto);
    }
}
