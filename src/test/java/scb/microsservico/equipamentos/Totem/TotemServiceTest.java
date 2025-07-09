package scb.microsservico.equipamentos.Totem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.TotemService;

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
    private Tranca trancaComBicicleta;
    private Tranca trancaSemBicicleta;
    private Bicicleta bicicleta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Praça da Sé");
        totem.setDescricao("Totem em frente à catedral");
        
        bicicleta = new Bicicleta();
        bicicleta.setId(10L);
        bicicleta.setNumero(101);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("10");
        bicicleta.setAno("2023");
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        trancaComBicicleta = new Tranca();
        trancaComBicicleta.setId(1L);
        trancaComBicicleta.setNumero(1);
        trancaComBicicleta.setBicicleta(bicicleta.getNumero());
        trancaComBicicleta.setStatus(TrancaStatus.OCUPADA);
        
        trancaSemBicicleta = new Tranca();
        trancaSemBicicleta.setId(2L);
        trancaSemBicicleta.setNumero(2);
        trancaSemBicicleta.setBicicleta(null);
        trancaSemBicicleta.setStatus(TrancaStatus.LIVRE);
        
        totem.setTrancas(List.of(trancaComBicicleta, trancaSemBicicleta));

        totemCreateDTO = new TotemCreateDTO();
        totemCreateDTO.setLocalizacao("Praça da Sé");
        totemCreateDTO.setDescricao("Totem em frente à catedral");

        totemUpdateDTO = new TotemUpdateDTO();
        totemUpdateDTO.setLocalizacao("Avenida Paulista");
        totemUpdateDTO.setDescricao("Totem próximo ao MASP");
    }

    @Test
    @DisplayName("Deve criar um totem com sucesso")
    void criarTotem_ComDadosValidos_DeveSalvarComSucesso() {
        // Arrange
        ArgumentCaptor<Totem> totemCaptor = ArgumentCaptor.forClass(Totem.class);

        // Act
        totemService.criarTotem(totemCreateDTO);

        // Assert
        verify(totemRepository, times(1)).save(totemCaptor.capture());
        Totem totemSalvo = totemCaptor.getValue();
        
        assertNotNull(totemSalvo);
        assertEquals(totemCreateDTO.getLocalizacao(), totemSalvo.getLocalizacao());
        assertEquals(totemCreateDTO.getDescricao(), totemSalvo.getDescricao());
    }

    @Test
    @DisplayName("Deve buscar um totem pelo ID e retornar DTO")
    void buscarTotemPorId_QuandoIdExiste_DeveRetornarTotemDTO() {
        // Arrange
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));

        // Act
        TotemResponseDTO resultado = totemService.buscarTotemPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(totem.getId(), resultado.getId());
        assertEquals(totem.getLocalizacao(), resultado.getLocalizacao());
    }

    @Test
    @DisplayName("Deve lançar TotemNotFoundException ao buscar totem com ID inexistente")
    void buscarTotemPorId_QuandoIdNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(totemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TotemNotFoundException.class, () -> totemService.buscarTotemPorId(99L));
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os totens")
    void buscarTodosTotens_DeveRetornarListaDeTotemDTO() {
        // Arrange
        when(totemRepository.findAll()).thenReturn(List.of(totem));

        // Act
        List<TotemResponseDTO> resultados = totemService.buscarTodosTotens();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(totem.getLocalizacao(), resultados.get(0).getLocalizacao());
    }
    
    @Test
    @DisplayName("Deve retornar uma lista vazia quando não há totens")
    void buscarTodosTotens_QuandoNaoExistemTotens_DeveRetornarListaVazia() {
        // Arrange
        when(totemRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TotemResponseDTO> resultados = totemService.buscarTodosTotens();

        // Assert
        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um totem com sucesso")
    void atualizarTotem_QuandoIdExiste_DeveRetornarTotemDTOAtualizado() {
        // Arrange
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        
        // Act
        TotemResponseDTO resultado = totemService.atualizarTotem(1L, totemUpdateDTO);

        // Assert
        verify(totemRepository, times(1)).save(any(Totem.class));
        assertNotNull(resultado);
        assertEquals(totemUpdateDTO.getLocalizacao(), resultado.getLocalizacao());
        assertEquals(totemUpdateDTO.getDescricao(), resultado.getDescricao());
    }
    
    @Test
    @DisplayName("Deve lançar TotemNotFoundException ao tentar atualizar totem inexistente")
    void atualizarTotem_QuandoIdNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(totemRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(TotemNotFoundException.class, () -> totemService.atualizarTotem(99L, totemUpdateDTO));
        verify(totemRepository, never()).save(any(Totem.class));
    }

    @Test
    @DisplayName("Deve deletar um totem com sucesso")
    void deletarTotem_QuandoIdExiste_DeveChamarDelete() {
        // Arrange
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        
        // Act
        totemService.deletarTotem(1L);

        // Assert
        verify(totemRepository, times(1)).delete(totem);
    }

    @Test
    @DisplayName("Deve lançar TotemNotFoundException ao tentar deletar totem inexistente")
    void deletarTotem_QuandoIdNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(totemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TotemNotFoundException.class, () -> totemService.deletarTotem(99L));
        verify(totemRepository, never()).delete(any(Totem.class));
    }

    @Test
    @DisplayName("Deve listar as trancas de um totem específico")
    void listarTrancasPorTotem_QuandoIdExiste_DeveRetornarListaDeTrancaDTO() {
        // Arrange
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));

        // Act
        List<TrancaResponseDTO> resultados = totemService.listarTrancasPorTotem(1L);

        // Assert
        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertEquals(trancaComBicicleta.getNumero(), resultados.get(0).getNumero());
    }

    @Test
    @DisplayName("Deve lançar TotemNotFoundException ao listar trancas de totem inexistente")
    void listarTrancasPorTotem_QuandoIdNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(totemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TotemNotFoundException.class, () -> totemService.listarTrancasPorTotem(99L));
    }

    @Test
    @DisplayName("Deve listar as bicicletas de um totem")
    void listarBicicletasDoTotem_QuandoTotemTemBicicletas_DeveRetornarListaDeBicicletaDTO() {
        // Arrange
        when(totemRepository.findById(1L)).thenReturn(Optional.of(totem));
        when(bicicletaRepository.findByNumero(bicicleta.getNumero())).thenReturn(Optional.of(bicicleta));
        
        // Act
        List<BicicletaResponseDTO> resultados = totemService.listarBicicletasDoTotem(1L);

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(bicicleta.getNumero(), resultados.get(0).getNumero());
        assertEquals(bicicleta.getModelo(), resultados.get(0).getModelo());
        
        verify(bicicletaRepository, times(1)).findByNumero(bicicleta.getNumero());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao listar bicicletas de totem inexistente")
    void listarBicicletasDoTotem_QuandoIdNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(totemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            totemService.listarBicicletasDoTotem(99L);
        });
        
        assertEquals("Totem não encontrado com o id: 99", exception.getMessage());
    }
}