package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.mapper.TotemMapper;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.TotemService;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TotemServiceTest {

    @Mock
    private TotemRepository totemRepository;
    
    @Mock
    private BicicletaRepository bicicletaRepository;

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

    void testListarTrancasPorTotem_Success() {
        Long idTotem = 1L;
        Totem totem = mock(Totem.class);
        Tranca tranca = mock(Tranca.class);
        TrancaResponseDTO trancaDTO = mock(TrancaResponseDTO.class);

        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));
        when(totem.getTrancas()).thenReturn(Collections.singletonList(tranca));

        try (MockedStatic<TrancaMapper> mapper = mockStatic(TrancaMapper.class)) {
            mapper.when(() -> TrancaMapper.toDTO(tranca)).thenReturn(trancaDTO);

            List<TrancaResponseDTO> result = totemService.listarTrancasPorTotem(idTotem);

            assertThat(result).containsExactly(trancaDTO);
        }
    }

    @Test
    void testListarTrancasPorTotem_NotFound() {
        Long idTotem = 1L;
        when(totemRepository.findById(idTotem)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> totemService.listarTrancasPorTotem(idTotem))
                .isInstanceOf(TotemNotFoundException.class);
    }

    @Test
    void testListarBicicletasDoTotem_Success() {
        // ... (your existing setup code for IDs, totem, trancas, etc.)
        Long idTotem = 1L;
        Integer numeroBicicleta = 123;

        Totem totem = mock(Totem.class);
        Tranca trancaComBicicleta = mock(Tranca.class);
        Tranca trancaSemBicicleta = mock(Tranca.class);
        Bicicleta bicicleta = mock(Bicicleta.class);
        BicicletaResponseDTO bicicletaDTO = mock(BicicletaResponseDTO.class);
        
        // Configuration of mocks
        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));
        when(totem.getTrancas()).thenReturn(Arrays.asList(trancaComBicicleta, trancaSemBicicleta));
        when(trancaComBicicleta.getBicicleta()).thenReturn(numeroBicicleta);
        when(trancaSemBicicleta.getBicicleta()).thenReturn(null);

        // Corrected line: Use the mocked instance 'bicicletaRepository'
        when(bicicletaRepository.findByNumero(numeroBicicleta)).thenReturn(Optional.of(bicicleta));

        try (MockedStatic<BicicletaMapper> mapper = mockStatic(BicicletaMapper.class)) {
            mapper.when(() -> BicicletaMapper.toDTO(bicicleta)).thenReturn(bicicletaDTO);
            
            // Execution
            List<BicicletaResponseDTO> result = totemService.listarBicicletasDoTotem(idTotem);

            // Verification
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(bicicletaDTO);
        }
    }
    
    @Test
    void testListarBicicletasDoTotem_TotemSemBicicletas() {
        Long idTotem = 1L;
        Totem totem = mock(Totem.class);
        Tranca tranca1 = mock(Tranca.class);
        Tranca tranca2 = mock(Tranca.class);

        when(totemRepository.findById(idTotem)).thenReturn(Optional.of(totem));
        when(totem.getTrancas()).thenReturn(Arrays.asList(tranca1, tranca2));
        when(tranca1.getBicicleta()).thenReturn(null);
        when(tranca2.getBicicleta()).thenReturn(null);

        List<BicicletaResponseDTO> result = totemService.listarBicicletasDoTotem(idTotem);

        assertThat(result).isEmpty();
    }
    
    @Test
    void testListarBicicletasDoTotem_TotemNotFound() {
        Long idTotem = 1L;
        when(totemRepository.findById(idTotem)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> totemService.listarBicicletasDoTotem(idTotem))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Totem não encontrado com o id: " + idTotem);
    }
}
