package scb.microsservico.equipamentos.Bicicleta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import scb.microsservico.equipamentos.controller.BicicletaController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.service.BicicletaService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BicicletaControllerTest {

    private MockMvc mockMvc;
    private BicicletaService bicicletaService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Mocka a camada de serviço, que é a dependência do nosso controller
        bicicletaService = Mockito.mock(BicicletaService.class);

        // Instancia o controller manualmente, injetando o serviço mockado
        BicicletaController bicicletaController = new BicicletaController(bicicletaService);
        
        // Configura o MockMvc em modo "standalone", sem carregar o contexto do Spring
        mockMvc = MockMvcBuilders.standaloneSetup(bicicletaController).build();
        
        // Utilitário para converter objetos Java em JSON
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /bicicleta - Deve criar bicicleta e retornar status 202 Accepted")
    void deveCriarBicicletaERetornarAccepted() throws Exception {
        // Arrange (Organizar)
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Caloi");
        dto.setModelo("10");
        dto.setAno("2023");
        
        doNothing().when(bicicletaService).criarBicicleta(any(BicicletaCreateDTO.class));
        
        // Act & Assert (Agir e Verificar)
        mockMvc.perform(post("/bicicleta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Bicicleta Cadastrada"));

        verify(bicicletaService).criarBicicleta(any(BicicletaCreateDTO.class));
    }
    
    @Test
    @DisplayName("GET /bicicleta/{id} - Deve buscar bicicleta por ID e retornar status 200 OK com o DTO")
    void deveBuscarBicicletaPorIdERetornarOk() throws Exception {
        // Arrange
        BicicletaResponseDTO responseDTO = new BicicletaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setMarca("Monark");
        responseDTO.setModelo("BMX");
        
        when(bicicletaService.buscarBicicletaPorId(1L)).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(get("/bicicleta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Monark"));
    }

    @Test
    @DisplayName("GET /bicicleta - Deve buscar todas as bicicletas e retornar status 200 OK com a lista")
    void deveBuscarTodasBicicletasERetornarOk() throws Exception {
        // Arrange
        BicicletaResponseDTO responseDTO = new BicicletaResponseDTO();
        responseDTO.setId(1L);
        List<BicicletaResponseDTO> lista = Collections.singletonList(responseDTO);

        when(bicicletaService.buscarTodasBicicletas()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("PUT /bicicleta/{id} - Deve atualizar a bicicleta e retornar status 200 OK com o DTO atualizado")
    void deveAtualizarBicicletaERetornarOk() throws Exception {
        // Arrange
        BicicletaUpdateDTO updateDTO = new BicicletaUpdateDTO();
        updateDTO.setMarca("Nova Marca");
        
        BicicletaResponseDTO responseDTO = new BicicletaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setMarca("Nova Marca");

        when(bicicletaService.atualizarBicicleta(eq(1L), any(BicicletaUpdateDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/bicicleta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Nova Marca"));
    }
    
    @Test
    @DisplayName("DELETE /bicicleta/{id} - Deve deletar a bicicleta e retornar status 202 Accepted")
    void deveDeletarBicicletaERetornarAccepted() throws Exception {
        // Arrange
        doNothing().when(bicicletaService).deletarBicicleta(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/bicicleta/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Bicicleta Deletada"));
        
        verify(bicicletaService).deletarBicicleta(1L);
    }

    @Test
    @DisplayName("POST /bicicleta/{id}/status/{acao} - Deve alterar o status e retornar 200 OK")
    void deveAlterarStatusERetornarOk() throws Exception {
        // Arrange
        Long idBicicleta = 1L;
        BicicletaStatus novoStatus = BicicletaStatus.EM_REPARO;
        
        BicicletaResponseDTO responseDTO = new BicicletaResponseDTO();
        responseDTO.setId(idBicicleta);
        responseDTO.setStatus(novoStatus);
        
        doNothing().when(bicicletaService).alterarStatus(idBicicleta, novoStatus);
        when(bicicletaService.buscarBicicletaPorId(idBicicleta)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/bicicleta/{idBicicleta}/status/{acao}", idBicicleta, novoStatus))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idBicicleta))
            .andExpect(jsonPath("$.status").value(novoStatus.toString()));
            
        verify(bicicletaService).alterarStatus(idBicicleta, novoStatus);
        verify(bicicletaService).buscarBicicletaPorId(idBicicleta);
    }
    
    @Test
    @DisplayName("POST /bicicleta/integrarNaRede - Deve integrar bicicleta e retornar 202 Accepted")
    void deveIntegrarBicicletaERetornarAccepted() throws Exception {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(10L);
        
        doNothing().when(bicicletaService).integrarBicicletaNaRede(any(IntegrarBicicletaDTO.class));
        
        // Act & Assert
        mockMvc.perform(post("/bicicleta/integrarNaRede")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isAccepted())
            .andExpect(content().string("Bicicleta Integrada"));
            
        verify(bicicletaService).integrarBicicletaNaRede(any(IntegrarBicicletaDTO.class));
    }

    @Test
    @DisplayName("POST /bicicleta/retirarDaRede - Deve retirar bicicleta e retornar 202 Accepted")
    void deveRetirarBicicletaERetornarAccepted() throws Exception {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdBicicleta(1L);
        dto.setIdTranca(10L);
        
        doNothing().when(bicicletaService).retirarBicicletaDaRede(any(RetirarBicicletaDTO.class));
        
        // Act & Assert
        mockMvc.perform(post("/bicicleta/retirarDaRede")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isAccepted())
            .andExpect(content().string("Bicicleta Retirada"));
            
        verify(bicicletaService).retirarBicicletaDaRede(any(RetirarBicicletaDTO.class));
    }
}