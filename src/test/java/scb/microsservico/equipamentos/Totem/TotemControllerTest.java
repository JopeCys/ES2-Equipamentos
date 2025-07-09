package scb.microsservico.equipamentos.Totem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import scb.microsservico.equipamentos.controller.TotemController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.service.TotemService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TotemControllerTest {

    private TotemService totemService;

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.totemService = Mockito.mock(TotemService.class);
        TotemController totemController = new TotemController(totemService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(totemController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve criar um totem e retornar status 202 Accepted")
    void deveCriarTotemComSucesso() throws Exception {
        TotemCreateDTO dto = new TotemCreateDTO();
        doNothing().when(totemService).criarTotem(any(TotemCreateDTO.class));

        mockMvc.perform(post("/totem") // URL CORRIGIDA
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        verify(totemService, times(1)).criarTotem(any(TotemCreateDTO.class));
    }

    @Test
    @DisplayName("Deve buscar totem por ID e retornar status 200 OK")
    void deveBuscarTotemPorIdComSucesso() throws Exception {
        Long idTotem = 1L;
        when(totemService.buscarTotemPorId(idTotem)).thenReturn(new TotemResponseDTO());

        mockMvc.perform(get("/totem/{id}", idTotem)) // URL CORRIGIDA
                .andExpect(status().isOk());

        verify(totemService, times(1)).buscarTotemPorId(idTotem);
    }

    @Test
    @DisplayName("Deve buscar todos os totens e retornar status 200 OK")
    void deveBuscarTodosOsTotens() throws Exception {
        when(totemService.buscarTodosTotens()).thenReturn(Collections.singletonList(new TotemResponseDTO()));

        mockMvc.perform(get("/totem")) // URL CORRIGIDA
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(totemService, times(1)).buscarTodosTotens();
    }

    @Test
    @DisplayName("Deve atualizar totem e retornar status 200 OK")
    void deveAtualizarTotemComSucesso() throws Exception {
        Long idTotem = 1L;
        TotemUpdateDTO dto = new TotemUpdateDTO();
        when(totemService.atualizarTotem(eq(idTotem), any(TotemUpdateDTO.class))).thenReturn(new TotemResponseDTO());

        mockMvc.perform(put("/totem/{id}", idTotem) // URL CORRIGIDA
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(totemService, times(1)).atualizarTotem(eq(idTotem), any(TotemUpdateDTO.class));
    }

    @Test
    @DisplayName("Deve deletar um totem e retornar status 202 Accepted")
    void deveDeletarTotemComSucesso() throws Exception {
        Long idTotem = 1L;
        doNothing().when(totemService).deletarTotem(idTotem);

        mockMvc.perform(delete("/totem/{id}", idTotem)) // URL CORRIGIDA
                .andExpect(status().isAccepted());

        verify(totemService, times(1)).deletarTotem(idTotem);
    }
    
    @Test
    @DisplayName("Deve listar as trancas de um totem e retornar status 200 OK")
    void deveListarTrancasDoTotemComSucesso() throws Exception {
        Long idTotem = 1L;
        when(totemService.listarTrancasPorTotem(idTotem)).thenReturn(Collections.singletonList(new TrancaResponseDTO()));

        mockMvc.perform(get("/totem/{id}/trancas", idTotem)) // URL CORRIGIDA
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(totemService, times(1)).listarTrancasPorTotem(idTotem);
    }

    @Test
    @DisplayName("Deve listar as bicicletas de um totem e retornar status 200 OK")
    void deveListarBicicletasDoTotemComSucesso() throws Exception {
        Long idTotem = 1L;
        when(totemService.listarBicicletasDoTotem(idTotem)).thenReturn(Collections.singletonList(new BicicletaResponseDTO()));

        mockMvc.perform(get("/totem/{id}/bicicletas", idTotem)) // URL CORRIGIDA
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(totemService, times(1)).listarBicicletasDoTotem(idTotem);
    }
}