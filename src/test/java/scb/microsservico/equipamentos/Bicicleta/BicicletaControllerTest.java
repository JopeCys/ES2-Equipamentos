package scb.microsservico.equipamentos.Bicicleta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scb.microsservico.equipamentos.controller.BicicletaController;
import scb.microsservico.equipamentos.dto.Bicicleta.*;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.service.BicicletaService;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BicicletaController.class)
public class BicicletaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BicicletaService bicicletaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCriarBicicleta() throws Exception {
        BicicletaCreateDTO bicicletaCreateDTO = new BicicletaCreateDTO();
        bicicletaCreateDTO.setMarca("Caloi");
        bicicletaCreateDTO.setModelo("10");
        bicicletaCreateDTO.setAno("2020");

        doNothing().when(bicicletaService).criarBicicleta(bicicletaCreateDTO);

        mockMvc.perform(post("/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bicicletaCreateDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testBuscarBicicletaPorId() throws Exception {
        BicicletaResponseDTO bicicletaResponseDTO = new BicicletaResponseDTO();
        bicicletaResponseDTO.setId(1L);

        when(bicicletaService.buscarBicicletaPorId(1L)).thenReturn(bicicletaResponseDTO);

        mockMvc.perform(get("/bicicleta/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBuscarTodasBicicletas() throws Exception {
        when(bicicletaService.buscarTodasBicicletas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAtualizarBicicleta() throws Exception {
        BicicletaUpdateDTO bicicletaUpdateDTO = new BicicletaUpdateDTO();
        bicicletaUpdateDTO.setMarca("Monark");
        bicicletaUpdateDTO.setModelo("BMX");
        bicicletaUpdateDTO.setAno("2022");

        BicicletaResponseDTO bicicletaResponseDTO = new BicicletaResponseDTO();
        bicicletaResponseDTO.setId(1L);
        bicicletaResponseDTO.setMarca("Monark");
        bicicletaResponseDTO.setModelo("BMX");
        bicicletaResponseDTO.setAno("2022");

        when(bicicletaService.atualizarBicicleta(1L, bicicletaUpdateDTO)).thenReturn(bicicletaResponseDTO);

        mockMvc.perform(put("/bicicleta/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bicicletaUpdateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletarBicicleta() throws Exception {
        doNothing().when(bicicletaService).deletarBicicleta(1L);

        mockMvc.perform(delete("/bicicleta/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testAlterarStatusBicicleta() throws Exception {
        doNothing().when(bicicletaService).alterarStatus(1L, BicicletaStatus.EM_REPARO);

        mockMvc.perform(post("/bicicleta/1/status/EM_REPARO"))
                .andExpect(status().isOk());
    }

    @Test
    public void testIntegrarBicicletaNaRede() throws Exception {
        IntegrarBicicletaDTO integrarBicicletaDTO = new IntegrarBicicletaDTO();
        integrarBicicletaDTO.setIdTranca(1L);
        integrarBicicletaDTO.setIdBicicleta(1L);
        integrarBicicletaDTO.setIdFuncionario(1L);

        doNothing().when(bicicletaService).integrarBicicletaNaRede(integrarBicicletaDTO);

        mockMvc.perform(post("/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrarBicicletaDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testRetirarBicicletaDaRede() throws Exception {
        RetirarBicicletaDTO retirarBicicletaDTO = new RetirarBicicletaDTO();
        retirarBicicletaDTO.setIdTranca(1L);
        retirarBicicletaDTO.setIdBicicleta(1L);
        retirarBicicletaDTO.setIdFuncionario(1L);
        retirarBicicletaDTO.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);


        doNothing().when(bicicletaService).retirarBicicletaDaRede(retirarBicicletaDTO);

        mockMvc.perform(post("/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarBicicletaDTO)))
                .andExpect(status().isAccepted());
    }
}