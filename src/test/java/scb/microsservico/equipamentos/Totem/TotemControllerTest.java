package scb.microsservico.equipamentos.Totem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import scb.microsservico.equipamentos.controller.TotemController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.service.TotemService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TotemController.class)
public class TotemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TotemService totemService;

    @Autowired
    private ObjectMapper objectMapper;

    private TotemCreateDTO totemCreateDTO;
    private TotemResponseDTO totemResponseDTO;
    private TotemUpdateDTO totemUpdateDTO;

    @BeforeEach
    void setUp() {
        totemCreateDTO = new TotemCreateDTO();
        totemCreateDTO.setLocalizacao("Totem Location");
        totemCreateDTO.setDescricao("Totem Description");

        totemResponseDTO = new TotemResponseDTO();
        totemResponseDTO.setId(1L);
        totemResponseDTO.setLocalizacao("Totem Location");
        totemResponseDTO.setDescricao("Totem Description");

        totemUpdateDTO = new TotemUpdateDTO();
        totemUpdateDTO.setLocalizacao("Updated Location");
        totemUpdateDTO.setDescricao("Updated Description");
    }

    @Test
    void testCriarTotem() throws Exception {
        doNothing().when(totemService).criarTotem(any(TotemCreateDTO.class));

        mockMvc.perform(post("/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totemCreateDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testBuscarTotemPorId() throws Exception {
        when(totemService.buscarTotemPorId(1L)).thenReturn(totemResponseDTO);

        mockMvc.perform(get("/totem/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.localizacao").value("Totem Location"));
    }

    @Test
    void testBuscarTodosTotens() throws Exception {
        when(totemService.buscarTodosTotens()).thenReturn(Collections.singletonList(totemResponseDTO));

        mockMvc.perform(get("/totem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testAtualizarTotem() throws Exception {
        when(totemService.atualizarTotem(eq(1L), any(TotemUpdateDTO.class))).thenReturn(totemResponseDTO);

        mockMvc.perform(put("/totem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totemUpdateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarTotem() throws Exception {
        doNothing().when(totemService).deletarTotem(1L);

        mockMvc.perform(delete("/totem/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void testListarTrancasDoTotem() throws Exception {
        TrancaResponseDTO trancaResponseDTO = new TrancaResponseDTO();
        trancaResponseDTO.setId(10L);
        when(totemService.listarTrancasPorTotem(1L)).thenReturn(Collections.singletonList(trancaResponseDTO));

        mockMvc.perform(get("/totem/1/trancas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    void testListarBicicletas() throws Exception {
        BicicletaResponseDTO bicicletaResponseDTO = new BicicletaResponseDTO();
        bicicletaResponseDTO.setId(20L);
        when(totemService.listarBicicletasDoTotem(1L)).thenReturn(Collections.singletonList(bicicletaResponseDTO));

        mockMvc.perform(get("/totem/1/bicicletas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20L));
    }
}