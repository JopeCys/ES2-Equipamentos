package scb.microsservico.equipamentos.Totem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.TotemService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotemServiceTest {

    @Mock
    private TotemRepository totemRepository;

    @Mock
    private BicicletaRepository bicicletaRepository;

    @InjectMocks
    private TotemService totemService;

    private Totem totem;
    private TotemCreateDTO totemCreateDTO;
    private TotemUpdateDTO totemUpdateDTO;

    @BeforeEach
    void setUp() {
        totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Localização Teste");
        totem.setDescricao("Descrição Teste");

        totemCreateDTO = new TotemCreateDTO();
        totemCreateDTO.setLocalizacao("Nova Localização");
        totemCreateDTO.setDescricao("Nova Descrição");

        totemUpdateDTO = new TotemUpdateDTO();
        totemUpdateDTO.setLocalizacao("Localização Atualizada");
        totemUpdateDTO.setDescricao("Descrição Atualizada");
    }

    @Test
    void criarTotem_deveSalvarTotem() {
        totemService.criarTotem(totemCreateDTO);
        verify(totemRepository, times(1)).save(any(Totem.class));
    }

    @Test
    void buscarTotemPorId_deveRetornarTotem_quandoEncontrado() {
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));

        TotemResponseDTO result = totemService.buscarTotemPorId(1L);

        assertNotNull(result);
        assertEquals(totem.getId(), result.getId());
    }

    @Test
    void buscarTotemPorId_deveLancarExcecao_quandoNaoEncontrado() {
        when(totemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TotemNotFoundException.class, () -> {
            totemService.buscarTotemPorId(1L);
        });
    }

    @Test
    void buscarTodosTotens_deveRetornarListaDeTotens() {
        when(totemRepository.findAll()).thenReturn(Collections.singletonList(totem));

        List<TotemResponseDTO> result = totemService.buscarTodosTotens();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void atualizarTotem_deveAtualizarDadosDoTotem() {
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);

        TotemResponseDTO result = totemService.atualizarTotem(1L, totemUpdateDTO);

        assertNotNull(result);
        assertEquals("Localização Atualizada", totem.getLocalizacao());
        assertEquals("Descrição Atualizada", totem.getDescricao());
    }

    @Test
    void deletarTotem_deveChamarMetodoDeletar() {
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        doNothing().when(totemRepository).delete(totem);

        totemService.deletarTotem(1L);

        verify(totemRepository, times(1)).delete(totem);
    }

    @Test
    void listarTrancasPorTotem_deveRetornarListaDeTrancas() {
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        totem.setTrancas(Collections.singletonList(tranca));
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));

        List<TrancaResponseDTO> result = totemService.listarTrancasPorTotem(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void listarBicicletasDoTotem_deveRetornarListaDeBicicletas() {
        Tranca tranca = new Tranca();
        tranca.setBicicleta(123);
        totem.setTrancas(Collections.singletonList(tranca));

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setNumero(123);

        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        when(bicicletaRepository.findByNumero(123)).thenReturn(Optional.of(bicicleta));

        List<BicicletaResponseDTO> result = totemService.listarBicicletasDoTotem(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}