package scb.microsservico.equipamentos.Bicicleta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.BicicletaService;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BicicletaServiceTest {

    private BicicletaRepository bicicletaRepository;
    private TrancaRepository trancaRepository;
    private BicicletaService bicicletaService;

    // Método de setup, executado antes de cada teste
    @BeforeEach
    void setUp() {
        // Criação dos mocks para os repositórios
        bicicletaRepository = Mockito.mock(BicicletaRepository.class);
        trancaRepository = Mockito.mock(TrancaRepository.class);
        
        // Instanciação manual do service com os mocks
        bicicletaService = new BicicletaService(bicicletaRepository, trancaRepository);
    }

    @Test
    @DisplayName("Deve criar uma nova bicicleta com sucesso")
    void deveCriarBicicletaComSucesso() {
        // Arrange (Organizar)
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Caloi");
        dto.setModelo("Cross");
        dto.setAno("1998");

        // Mock para o repositório não encontrar número de bicicleta duplicado
        when(bicicletaRepository.existsByNumero(any(Integer.class))).thenReturn(false);
        // Mock para o save, retornando a bicicleta salva
        when(bicicletaRepository.save(any(Bicicleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act (Agir)
        bicicletaService.criarBicicleta(dto);

        // Assert (Verificar)
        // Captura o argumento passado para o método save
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        
        Bicicleta bicicletaSalva = bicicletaCaptor.getValue();
        assertNotNull(bicicletaSalva.getNumero());
        assertEquals(BicicletaStatus.NOVA, bicicletaSalva.getStatus());
        assertEquals("Caloi", bicicletaSalva.getMarca());
    }

    @Test
    @DisplayName("Deve buscar uma bicicleta por ID e retorná-la")
    void deveBuscarBicicletaPorId() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(123);
        bicicleta.setMarca("Monark");
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act
        var responseDTO = bicicletaService.buscarBicicletaPorId(1L);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals(123, responseDTO.getNumero());
        assertEquals("Monark", responseDTO.getMarca());
    }

    @Test
    @DisplayName("Deve lançar BicicletaNotFoundException quando buscar ID inexistente")
    void deveLancarExcecaoQuandoBuscarBicicletaInexistente() {
        // Arrange
        when(bicicletaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BicicletaNotFoundException.class, () -> {
            bicicletaService.buscarBicicletaPorId(99L);
        });
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as bicicletas")
    void deveBuscarTodasBicicletas() {
        // Arrange
        Bicicleta b1 = new Bicicleta();
        b1.setId(1L);
        Bicicleta b2 = new Bicicleta();
        b2.setId(2L);
        
        when(bicicletaRepository.findAll()).thenReturn(List.of(b1, b2));

        // Act
        var listaBicicletas = bicicletaService.buscarTodasBicicletas();

        // Assert
        assertNotNull(listaBicicletas);
        assertEquals(2, listaBicicletas.size());
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma bicicleta")
    void deveAtualizarBicicleta() {
        // Arrange
        Bicicleta bicicletaExistente = new Bicicleta();
        bicicletaExistente.setId(1L);
        bicicletaExistente.setMarca("Marca Antiga");
        bicicletaExistente.setModelo("Modelo Antigo");
        bicicletaExistente.setAno("2020");

        BicicletaUpdateDTO dto = new BicicletaUpdateDTO();
        dto.setMarca("Marca Nova");
        dto.setModelo("Modelo Novo");
        dto.setAno("2024");
        
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicletaExistente));
        
        // Act
        bicicletaService.atualizarBicicleta(1L, dto);

        // Assert
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());

        Bicicleta bicicletaAtualizada = bicicletaCaptor.getValue();
        assertEquals("Marca Nova", bicicletaAtualizada.getMarca());
        assertEquals("Modelo Novo", bicicletaAtualizada.getModelo());
        assertEquals("2024", bicicletaAtualizada.getAno());
    }

    @Test
    @DisplayName("Deve deletar (aposentar) uma bicicleta com sucesso")
    void deveDeletarBicicletaComSucesso() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL); // Status que permite deleção
        
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act
        bicicletaService.deletarBicicleta(1L);

        // Assert
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        
        Bicicleta bicicletaAposentada = bicicletaCaptor.getValue();
        assertEquals(BicicletaStatus.APOSENTADA, bicicletaAposentada.getStatus());
    }

    @Test
    @DisplayName("Deve lançar BicicletaOcupadaException ao tentar deletar bicicleta EM_USO")
    void deveLancarExcecaoAoDeletarBicicletaEmUso() {
        // Arrange
        Bicicleta bicicletaEmUso = new Bicicleta();
        bicicletaEmUso.setId(1L);
        bicicletaEmUso.setStatus(BicicletaStatus.EM_USO);
        
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicletaEmUso));

        // Act & Assert
        assertThrows(BicicletaOcupadaException.class, () -> {
            bicicletaService.deletarBicicleta(1L);
        });

        // Verifica que o save não foi chamado
        verify(bicicletaRepository, never()).save(any(Bicicleta.class));
    }
    
    @Test
    @DisplayName("Deve alterar o status de uma bicicleta")
    void deveAlterarStatus() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        
        // Act
        bicicletaService.alterarStatus(1L, BicicletaStatus.EM_REPARO);
        
        // Assert
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        
        Bicicleta bicicletaComStatusAlterado = bicicletaCaptor.getValue();
        assertEquals(BicicletaStatus.EM_REPARO, bicicletaComStatusAlterado.getStatus());
    }

    @Test
    @DisplayName("Deve integrar bicicleta na rede com sucesso")
    void deveIntegrarBicicletaNaRede() {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(10L);
        dto.setIdBicicleta(20L);

        Tranca tranca = new Tranca();
        tranca.setId(10L);
        tranca.setStatus(TrancaStatus.LIVRE); // Tranca deve estar livre
        tranca.setBicicleta(null);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(20L);
        bicicleta.setNumero(9876);
        bicicleta.setStatus(BicicletaStatus.NOVA); // Bicicleta deve estar apta
        
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(20L)).thenReturn(Optional.of(bicicleta));
        
        // Act
        bicicletaService.integrarBicicletaNaRede(dto);
        
        // Assert
        // Captura da tranca
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);
        verify(trancaRepository).save(trancaCaptor.capture());
        Tranca trancaAtualizada = trancaCaptor.getValue();
        assertEquals(TrancaStatus.OCUPADA, trancaAtualizada.getStatus());
        assertEquals(9876, trancaAtualizada.getBicicleta());

        // Captura da bicicleta
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        Bicicleta bicicletaAtualizada = bicicletaCaptor.getValue();
        assertEquals(BicicletaStatus.DISPONIVEL, bicicletaAtualizada.getStatus());
    }
    
    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar integrar em tranca ocupada")
    void deveLancarExcecaoAoIntegrarEmTrancaOcupada() {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(10L);
        dto.setIdBicicleta(20L);

        Tranca trancaOcupada = new Tranca();
        trancaOcupada.setId(10L);
        trancaOcupada.setStatus(TrancaStatus.OCUPADA); // Tranca ocupada
        trancaOcupada.setBicicleta(1111);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(20L);
        bicicleta.setStatus(BicicletaStatus.NOVA);
        
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(trancaOcupada));
        when(bicicletaRepository.findById(20L)).thenReturn(Optional.of(bicicleta));
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });
    }

    @Test
    @DisplayName("Deve retirar bicicleta da rede com sucesso")
    void deveRetirarBicicletaDaRede() {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(10L);
        dto.setIdBicicleta(20L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(20L);
        bicicleta.setNumero(12345);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        
        Tranca tranca = new Tranca();
        tranca.setId(10L);
        tranca.setBicicleta(12345); // Bicicleta correta na tranca
        tranca.setStatus(TrancaStatus.OCUPADA);
        
        when(bicicletaRepository.findById(20L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(tranca));
        
        // Act
        bicicletaService.retirarBicicletaDaRede(dto);
        
        // Assert
        // Captura da Tranca
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);
        verify(trancaRepository).save(trancaCaptor.capture());
        Tranca trancaAtualizada = trancaCaptor.getValue();
        assertEquals(TrancaStatus.LIVRE, trancaAtualizada.getStatus());
        assertNull(trancaAtualizada.getBicicleta());
        
        // Captura da Bicicleta
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        Bicicleta bicicletaAtualizada = bicicletaCaptor.getValue();
        assertEquals(BicicletaStatus.EM_USO, bicicletaAtualizada.getStatus());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar retirar de tranca com bicicleta errada")
    void deveLancarExcecaoAoRetirarComBicicletaErrada() {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(10L);
        dto.setIdBicicleta(20L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(20L);
        bicicleta.setNumero(12345); // Número da bicicleta é 12345

        Tranca tranca = new Tranca();
        tranca.setId(10L);
        tranca.setBicicleta(99999); // Tranca está com outra bicicleta (99999)
        tranca.setStatus(TrancaStatus.OCUPADA);
        
        when(bicicletaRepository.findById(20L)).thenReturn(Optional.of(bicicleta));
        when(trancaRepository.findById(10L)).thenReturn(Optional.of(tranca));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });

        assertTrue(exception.getMessage().contains("não corresponde à bicicleta registrada na tranca"));
    }
}