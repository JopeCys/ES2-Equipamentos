package scb.microsservico.equipamentos.Tranca;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import scb.microsservico.equipamentos.controller.TrancaController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.*;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class TrancaControllerTest {

    // Objeto para simular as requisições HTTP. Será inicializado manualmente.
    private MockMvc mockMvc;

    // Mock da camada de serviço. Usamos o Mockito diretamente.
    private TrancaService trancaService;
    
    // Objeto para converter DTOs em JSON. Inicializado manualmente.
    private final ObjectMapper objectMapper = new ObjectMapper();

    private TrancaResponseDTO trancaResponseDTO;
    private BicicletaResponseDTO bicicletaResponseDTO;

    @BeforeEach
    void setUp() {
        // 1. Criamos o mock do serviço usando Mockito.
        trancaService = Mockito.mock(TrancaService.class);
        
        // 2. Instanciamos o controller manualmente, injetando o mock do serviço no construtor.
        //    Isso funciona por causa da anotação @RequiredArgsConstructor no controller.
        TrancaController trancaController = new TrancaController(trancaService);

        // 3. Construímos o MockMvc no modo standalone, passando a instância do nosso controller.
        //    Isso cria um ambiente de teste mínimo apenas para este controller.
        mockMvc = MockMvcBuilders.standaloneSetup(trancaController).build();

        // Configuração dos DTOs de resposta para os testes (igual ao exemplo anterior)
        trancaResponseDTO = new TrancaResponseDTO();
        trancaResponseDTO.setId(1L);
        trancaResponseDTO.setNumero(10);
        trancaResponseDTO.setModelo("Modelo Forte");
        trancaResponseDTO.setAnoDeFabricacao("2023");
        trancaResponseDTO.setStatus(TrancaStatus.LIVRE);

        bicicletaResponseDTO = new BicicletaResponseDTO();
        bicicletaResponseDTO.setId(100L);
        bicicletaResponseDTO.setMarca("Caloi");
        bicicletaResponseDTO.setModelo("Caloi 10");
        bicicletaResponseDTO.setStatus(BicicletaStatus.DISPONIVEL);
    }
    
    @Test
    @DisplayName("Deve criar uma tranca com sucesso")
    void criarTranca_ComDadosValidos_DeveRetornarAccepted() throws Exception {
        // ARRANGE
        TrancaCreateDTO createDTO = new TrancaCreateDTO();
        createDTO.setNumero(10);
        createDTO.setModelo("Modelo Forte");
        createDTO.setAnoDeFabricacao("2023");
        doNothing().when(trancaService).criarTranca(any(TrancaCreateDTO.class));
        
        // ACT & ASSERT
        mockMvc.perform(post("/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Dados Cadastrados"));
    }

    @Test
    @DisplayName("Deve buscar uma tranca por ID e retorná-la")
    void buscarTrancaPorId_QuandoEncontrada_DeveRetornarTrancaResponseDTO() throws Exception {
        // ARRANGE
        Long idTranca = 1L;
        when(trancaService.buscarTrancaPorId(idTranca)).thenReturn(trancaResponseDTO);

        // ACT & ASSERT
        mockMvc.perform(get("/tranca/{idTranca}", idTranca))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.modelo", is("Modelo Forte")));
    }

    @Test
    @DisplayName("Deve buscar todas as trancas e retornar uma lista")
    void buscarTodasTrancas_DeveRetornarListaDeTrancas() throws Exception {
        // ARRANGE
        List<TrancaResponseDTO> trancas = Collections.singletonList(trancaResponseDTO);
        when(trancaService.buscarTodasTrancas()).thenReturn(trancas);

        // ACT & ASSERT
        mockMvc.perform(get("/tranca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    // ... todos os outros testes permanecem exatamente iguais ...

    @Test
    @DisplayName("Deve atualizar uma tranca com sucesso")
    void atualizarTranca_ComDadosValidos_DeveRetornarTrancaAtualizada() throws Exception {
        // ARRANGE
        Long idTranca = 1L;
        TrancaUpdateDTO updateDTO = new TrancaUpdateDTO();
        updateDTO.setModelo("Modelo Atualizado");
        
        TrancaResponseDTO trancaAtualizada = new TrancaResponseDTO();
        trancaAtualizada.setId(idTranca);
        trancaAtualizada.setModelo("Modelo Atualizado");
        trancaAtualizada.setStatus(TrancaStatus.LIVRE);

        when(trancaService.atualizarTranca(eq(idTranca), any(TrancaUpdateDTO.class))).thenReturn(trancaAtualizada);

        // ACT & ASSERT
        mockMvc.perform(put("/tranca/{idTranca}", idTranca)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo", is("Modelo Atualizado")));
    }

    @Test
    @DisplayName("Deve deletar uma tranca com sucesso")
    void deletarTranca_DeveRetornarAccepted() throws Exception {
        // ARRANGE
        Long idTranca = 1L;
        doNothing().when(trancaService).deletarTranca(idTranca);
        
        // ACT & ASSERT
        mockMvc.perform(delete("/tranca/{idTranca}", idTranca))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca Deletada"));
    }
}