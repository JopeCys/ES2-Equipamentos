package scb.microsservico.equipamentos.Totem;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import scb.microsservico.equipamentos.controller.TotemController;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.service.TotemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TotemControllerTest {

    @Mock
    private TotemService totemService;

    @InjectMocks
    private TotemController totemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarTotem() {
        TotemCreateDTO dto = new TotemCreateDTO();
        doNothing().when(totemService).criarTotem(dto);

        ResponseEntity<String> response = totemController.criarTotem(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(202);
        assertThat(response.getBody()).isEqualTo("Totem Cadastrado");
        verify(totemService, times(1)).criarTotem(dto);
    }

    @Test
    void testBuscarTotemPorId() {
        Long id = 1L;
        TotemResponseDTO mockResponse = new TotemResponseDTO();
        when(totemService.buscarTotemPorId(id)).thenReturn(mockResponse);

        ResponseEntity<TotemResponseDTO> response = totemController.buscarTotemPorId(id);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);
        verify(totemService, times(1)).buscarTotemPorId(id);
    }

    @Test
    void testBuscarTodosTotens() {
        TotemResponseDTO t1 = new TotemResponseDTO();
        TotemResponseDTO t2 = new TotemResponseDTO();
        List<TotemResponseDTO> list = Arrays.asList(t1, t2);
        when(totemService.buscarTodosTotens()).thenReturn(list);

        ResponseEntity<List<TotemResponseDTO>> response = totemController.buscarTodosTotens();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(t1, t2);
        verify(totemService, times(1)).buscarTodosTotens();
    }

    @Test
    void testAtualizarTotem() {
        Long id = 1L;
        TotemUpdateDTO dto = new TotemUpdateDTO();
        TotemResponseDTO updated = new TotemResponseDTO();
        when(totemService.atualizarTotem(id, dto)).thenReturn(updated);

        ResponseEntity<TotemResponseDTO> response = totemController.atualizarTotem(id, dto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(totemService, times(1)).atualizarTotem(id, dto);
    }

    @Test
    void testDeletarTotem() {
        Long id = 1L;
        doNothing().when(totemService).deletarTotem(id);

        ResponseEntity<String> response = totemController.deletarTotem(id);

        assertThat(response.getStatusCode().value()).isEqualTo(202);
        assertThat(response.getBody()).isEqualTo("Totem Deletado");
        verify(totemService, times(1)).deletarTotem(id);
    }

     @Test
    void testListarTrancasDoTotem() {
        // Arrange
        Long idTotem = 1L;
        List<TrancaResponseDTO> mockTrancasList = Collections.singletonList(new TrancaResponseDTO());
        when(totemService.listarTrancasPorTotem(idTotem)).thenReturn(mockTrancasList);

        // Act
        ResponseEntity<List<TrancaResponseDTO>> response = totemController.listarTrancasDoTotem(idTotem);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockTrancasList);
        verify(totemService, times(1)).listarTrancasPorTotem(idTotem);
    }

    @Test
    void testListarBicicletasDoTotem() {
        // Arrange
        Long idTotem = 1L;
        List<BicicletaResponseDTO> mockBicicletasList = Collections.singletonList(new BicicletaResponseDTO());
        when(totemService.listarBicicletasDoTotem(idTotem)).thenReturn(mockBicicletasList);

        // Act
        ResponseEntity<List<BicicletaResponseDTO>> response = totemController.listarBicicletas(idTotem);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockBicicletasList);
        verify(totemService, times(1)).listarBicicletasDoTotem(idTotem);
    }
}
