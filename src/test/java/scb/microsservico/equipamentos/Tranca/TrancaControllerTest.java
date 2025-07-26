package scb.microsservico.equipamentos.Tranca;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scb.microsservico.equipamentos.controller.TrancaController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.*;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TrancaController.class)
class TrancaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrancaService trancaService;

    @Autowired
    private ObjectMapper objectMapper;

    private TrancaCreateDTO trancaCreateDTO;
    private TrancaResponseDTO trancaResponseDTO;
    private TrancaUpdateDTO trancaUpdateDTO;

    @BeforeEach
    void setUp() {
        trancaCreateDTO = new TrancaCreateDTO();
        trancaCreateDTO.setNumero(100);
        trancaCreateDTO.setModelo("Modelo Teste");
        trancaCreateDTO.setAnoDeFabricacao("2023");

        trancaResponseDTO = new TrancaResponseDTO();
        trancaResponseDTO.setId(1L);
        trancaResponseDTO.setNumero(100);
        trancaResponseDTO.setModelo("Modelo Teste");
        trancaResponseDTO.setAnoDeFabricacao("2023");
        trancaResponseDTO.setStatus(TrancaStatus.LIVRE);

        trancaUpdateDTO = new TrancaUpdateDTO();
        trancaUpdateDTO.setModelo("Modelo Atualizado");
        trancaUpdateDTO.setAnoDeFabricacao("2024");
    }

    @Test
    void testCriarTranca() throws Exception {
        doNothing().when(trancaService).criarTranca(any(TrancaCreateDTO.class));

        mockMvc.perform(post("/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trancaCreateDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testBuscarTrancaPorId() throws Exception {
        when(trancaService.buscarTrancaPorId(1L)).thenReturn(trancaResponseDTO);

        mockMvc.perform(get("/tranca/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.modelo").value("Modelo Teste"));
    }

    @Test
    void testBuscarTodasTrancas() throws Exception {
        when(trancaService.buscarTodasTrancas()).thenReturn(Collections.singletonList(trancaResponseDTO));

        mockMvc.perform(get("/tranca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testAtualizarTranca() throws Exception {
        when(trancaService.atualizarTranca(eq(1L), any(TrancaUpdateDTO.class))).thenReturn(trancaResponseDTO);

        mockMvc.perform(put("/tranca/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trancaUpdateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarTranca() throws Exception {
        doNothing().when(trancaService).deletarTranca(1L);

        mockMvc.perform(delete("/tranca/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void testBuscarBicicletaNaTranca() throws Exception {
        BicicletaResponseDTO bicicletaResponseDTO = new BicicletaResponseDTO();
        bicicletaResponseDTO.setId(5L);

        when(trancaService.buscarBicicletaNaTranca(1L)).thenReturn(bicicletaResponseDTO);

        mockMvc.perform(get("/tranca/1/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void testTrancarTranca() throws Exception {
        TrancarRequestDTO trancarRequestDTO = new TrancarRequestDTO();
        trancarRequestDTO.setBicicleta(5L);

        doNothing().when(trancaService).trancarTranca(eq(1L), any(TrancarRequestDTO.class));

        mockMvc.perform(post("/tranca/1/trancar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trancarRequestDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testDestrancarTranca() throws Exception {
        DestrancarRequestDTO destrancarRequestDTO = new DestrancarRequestDTO();
        destrancarRequestDTO.setBicicleta(5L);

        doNothing().when(trancaService).destrancarTranca(eq(1L), any(DestrancarRequestDTO.class));

        mockMvc.perform(post("/tranca/1/destrancar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destrancarRequestDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testAlterarStatusTranca() throws Exception {
        doNothing().when(trancaService).alterarStatus(1L, TrancaStatus.EM_REPARO);
        when(trancaService.buscarTrancaPorId(1L)).thenReturn(trancaResponseDTO);


        mockMvc.perform(post("/tranca/1/status/EM_REPARO"))
                .andExpect(status().isOk());

        verify(trancaService, times(1)).alterarStatus(1L, TrancaStatus.EM_REPARO);
    }

    @Test
    void testIntegrarNaRede() throws Exception {
        IntegrarTrancaDTO integrarTrancaDTO = new IntegrarTrancaDTO();
        integrarTrancaDTO.setIdTranca(1L);
        integrarTrancaDTO.setIdTotem(10L);
        integrarTrancaDTO.setIdFuncionario(100L);

        doNothing().when(trancaService).integrarNaRede(any(IntegrarTrancaDTO.class));

        mockMvc.perform(post("/tranca/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrarTrancaDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testRetirarDaRede() throws Exception {
        RetirarTrancaDTO retirarTrancaDTO = new RetirarTrancaDTO();
        retirarTrancaDTO.setIdTranca(1L);
        retirarTrancaDTO.setIdTotem(10L);
        retirarTrancaDTO.setIdFuncionario(100L);
        retirarTrancaDTO.setStatusAcaoReparador(AcaoRetirar.APOSENTADORIA);

        doNothing().when(trancaService).retirarDaRede(any(RetirarTrancaDTO.class));

        mockMvc.perform(post("/tranca/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarTrancaDTO)))
                .andExpect(status().isAccepted());
    }
}