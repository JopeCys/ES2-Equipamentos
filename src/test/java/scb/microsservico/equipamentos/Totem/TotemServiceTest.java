package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.mapper.TotemMapper;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.TotemService;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TotemServiceTest {

    @Mock
    private TotemRepository totemRepository;

    @InjectMocks
    private TotemService totemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarTotem() {
        TotemCreateDTO dto = mock(TotemCreateDTO.class);
        Totem totem = mock(Totem.class);

        try (MockedStatic<TotemMapper> mapper = mockStatic(TotemMapper.class)) {
            mapper.when(() -> TotemMapper.toEntity(dto)).thenReturn(totem);
            when(totemRepository.save(totem)).thenReturn(totem);

            totemService.criarTotem(dto);

            verify(totemRepository, times(1)).save(totem);
        }
    }

    @Test
    void testBuscarTotemPorId_Success() {
        Long id = 1L;
        Totem totem = mock(Totem.class);
        TotemResponseDTO dto = mock(TotemResponseDTO.class);

        when(totemRepository.findById(id)).thenReturn(Optional.of(totem));
        try (MockedStatic<TotemMapper> mapper = mockStatic(TotemMapper.class)) {
            mapper.when(() -> TotemMapper.toDTO(totem)).thenReturn(dto);

            TotemResponseDTO result = totemService.buscarTotemPorId(id);

            assertThat(result).isEqualTo(dto);
        }
    }

    @Test
    void testBuscarTotemPorId_NotFound() {
        Long id = 1L;
        when(totemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> totemService.buscarTotemPorId(id))
                .isInstanceOf(TotemNotFoundException.class);
    }

    @Test
    void testBuscarTodosTotens() {
        Totem t1 = mock(Totem.class);
        Totem t2 = mock(Totem.class);
        List<Totem> totens = Arrays.asList(t1, t2);
        TotemResponseDTO dto1 = mock(TotemResponseDTO.class);
        TotemResponseDTO dto2 = mock(TotemResponseDTO.class);

        when(totemRepository.findAll()).thenReturn(totens);
        try (MockedStatic<TotemMapper> mapper = mockStatic(TotemMapper.class)) {
            mapper.when(() -> TotemMapper.toDTO(t1)).thenReturn(dto1);
            mapper.when(() -> TotemMapper.toDTO(t2)).thenReturn(dto2);

            List<TotemResponseDTO> result = totemService.buscarTodosTotens();

            assertThat(result).containsExactly(dto1, dto2);
        }
    }

    @Test
    void testAtualizarTotem_Success() {
        Long id = 1L;
        TotemUpdateDTO dto = mock(TotemUpdateDTO.class);
        Totem totem = mock(Totem.class);
        TotemResponseDTO responseDTO = mock(TotemResponseDTO.class);

        when(totemRepository.findById(id)).thenReturn(Optional.of(totem));
        when(dto.getLocalizacao()).thenReturn("Local");
        when(dto.getDescricao()).thenReturn("Desc");
        when(totemRepository.save(totem)).thenReturn(totem);

        try (MockedStatic<TotemMapper> mapper = mockStatic(TotemMapper.class)) {
            mapper.when(() -> TotemMapper.toDTO(totem)).thenReturn(responseDTO);

            TotemResponseDTO result = totemService.atualizarTotem(id, dto);

            verify(totem).setLocalizacao("Local");
            verify(totem).setDescricao("Desc");
            verify(totemRepository).save(totem);
            assertThat(result).isEqualTo(responseDTO);
        }
    }

    @Test
    void testAtualizarTotem_NotFound() {
        Long id = 1L;
        TotemUpdateDTO dto = mock(TotemUpdateDTO.class);
        when(totemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> totemService.atualizarTotem(id, dto))
                .isInstanceOf(TotemNotFoundException.class);
    }

    @Test
    void testDeletarTotem_Success() {
        Long id = 1L;
        Totem totem = mock(Totem.class);
        when(totemRepository.findById(id)).thenReturn(Optional.of(totem));

        totemService.deletarTotem(id);

        verify(totemRepository).delete(totem);
    }

    @Test
    void testDeletarTotem_NotFound() {
        Long id = 1L;
        when(totemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> totemService.deletarTotem(id))
                .isInstanceOf(TotemNotFoundException.class);
    }
}
