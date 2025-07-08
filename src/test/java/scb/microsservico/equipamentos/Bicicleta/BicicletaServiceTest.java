package scb.microsservico.equipamentos.Bicicleta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
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


public class BicicletaServiceTest {

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TrancaRepository trancaRepository; // Adicionado mock para TrancaRepository
    
    @InjectMocks
    private BicicletaService bicicletaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarBicicleta() {
        BicicletaCreateDTO dto = new BicicletaCreateDTO();
        dto.setMarca("Caloi");
        dto.setModelo("Elite");
        dto.setAno("2022"); // Alterado para String

        ArgumentCaptor<Bicicleta> captor = ArgumentCaptor.forClass(Bicicleta.class);

        bicicletaService.criarBicicleta(dto);

        verify(bicicletaRepository).save(captor.capture());
        Bicicleta saved = captor.getValue();
        assertEquals("Caloi", saved.getMarca());
        assertEquals("Elite", saved.getModelo());
        assertEquals("2022", saved.getAno()); // Alterado para String
        assertEquals(BicicletaStatus.NOVA, saved.getStatus());
        assertNotNull(saved.getNumero());
    }

    @Test
    void testBuscarBicicletaPorId_Success() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2022"); // Alterado para String
        bicicleta.setStatus(BicicletaStatus.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        BicicletaResponseDTO dto = bicicletaService.buscarBicicletaPorId(1L);

        assertEquals("Caloi", dto.getMarca());
        assertEquals("Elite", dto.getModelo());
        assertEquals("2022", dto.getAno()); // Alterado para String
    }

    @Test
    void testBuscarBicicletaPorId_NotFound() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BicicletaNotFoundException.class, () -> bicicletaService.buscarBicicletaPorId(1L));
    }

    @Test
    void testBuscarTodasBicicletas() {
        Bicicleta b1 = new Bicicleta();
        b1.setMarca("Caloi");
        b1.setModelo("Elite");
        b1.setAno("2022"); // Alterado para String

        Bicicleta b2 = new Bicicleta();
        b2.setMarca("Monark");
        b2.setModelo("Classic");
        b2.setAno("2020"); // Alterado para String

        when(bicicletaRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<BicicletaResponseDTO> result = bicicletaService.buscarTodasBicicletas();
        assertEquals(2, result.size());
        assertEquals("Caloi", result.get(0).getMarca());
        assertEquals("Monark", result.get(1).getMarca());
    }

    @Test
    void testAtualizarBicicleta_Success() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("Elite");
        bicicleta.setAno("2022"); // Alterado para String

        BicicletaUpdateDTO dto = new BicicletaUpdateDTO();
        dto.setMarca("Monark");
        dto.setModelo("Classic");
        dto.setAno("2023"); // Alterado para String

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(bicicletaRepository.save(any(Bicicleta.class))).thenReturn(bicicleta);

        BicicletaResponseDTO response = bicicletaService.atualizarBicicleta(1L, dto);

        assertEquals("Monark", response.getMarca());
        assertEquals("Classic", response.getModelo());
        assertEquals("2023", response.getAno()); // Alterado para String
    }

    @Test
    void testAtualizarBicicleta_NotFound() {
        BicicletaUpdateDTO dto = new BicicletaUpdateDTO();
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BicicletaNotFoundException.class, () -> bicicletaService.atualizarBicicleta(1L, dto));
    }

    @Test
    void testDeletarBicicleta_Success() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.NOVA);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        bicicletaService.deletarBicicleta(1L);

        assertEquals(BicicletaStatus.APOSENTADA, bicicleta.getStatus());
        verify(bicicletaRepository).save(bicicleta);
    }

    @Test
    void testDeletarBicicleta_EmUso() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.EM_USO);

        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        assertThrows(BicicletaOcupadaException.class, () -> bicicletaService.deletarBicicleta(1L));
    }

    @Test
    void testDeletarBicicleta_NotFound() {
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BicicletaNotFoundException.class, () -> bicicletaService.deletarBicicleta(1L));
    }

    @Test
    void testAlterarStatus_Success() {
        // Arrange
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.NOVA);
        
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));
        
        // Act
        bicicletaService.alterarStatus(1L, BicicletaStatus.EM_REPARO);

        // Assert
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        Bicicleta savedBicicleta = bicicletaCaptor.getValue();

        assertEquals(BicicletaStatus.EM_REPARO, savedBicicleta.getStatus());
    }

    @Test
    void testAlterarStatus_NotFound() {
        // Arrange
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BicicletaNotFoundException.class, () -> {
            bicicletaService.alterarStatus(1L, BicicletaStatus.DISPONIVEL);
        });
    }

    @Test
    void testIntegrarBicicletaNaRede_Success() {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(12345);
        bicicleta.setStatus(BicicletaStatus.NOVA); // Status inicial correto para o teste

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.LIVRE); // Status inicial correto para o teste
        
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act
        bicicletaService.integrarBicicletaNaRede(dto);

        // Assert
        // Verifica o estado final esperado dos objetos após a execução do serviço

        // O status da bicicleta deve ser DISPONIVEL
        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
        
        // CORREÇÃO: O status da tranca deve ser OCUPADA
        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        
        // O número da bicicleta na tranca deve ser o número da bicicleta integrada
        assertEquals(bicicleta.getNumero(), tranca.getNumerobicicleta());
        
        // Verifica se os métodos save foram chamados uma vez para cada repositório
        verify(bicicletaRepository).save(bicicleta);
        verify(trancaRepository).save(tranca);
    }

    
    @Test
    void testIntegrarBicicletaNaRede_TrancaOcupada() {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        Tranca trancaOcupada = new Tranca();
        trancaOcupada.setId(1L);
        trancaOcupada.setStatus(TrancaStatus.OCUPADA);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(trancaOcupada));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });
    }

    @Test
    void testIntegrarBicicletaNaRede_StatusBicicletaInvalido() {
        // Arrange
        IntegrarBicicletaDTO dto = new IntegrarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL); // Status inválido para integração

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.LIVRE);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.integrarBicicletaNaRede(dto);
        });
    }

    @Test
    void testRetirarBicicletaDaRede_Success() {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(12345);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        
        // ****** AQUI ESTÁ A CORREÇÃO ******
        tranca.setNumerobicicleta(bicicleta.getNumero()); // Usando o setter correto

        tranca.setStatus(TrancaStatus.OCUPADA);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act
        bicicletaService.retirarBicicletaDaRede(dto);

        // Assert
        assertEquals(BicicletaStatus.EM_USO, bicicleta.getStatus());
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertNull(tranca.getNumerobicicleta());
        verify(bicicletaRepository).save(bicicleta);
        verify(trancaRepository).save(tranca);
    }

    @Test
    void testRetirarBicicletaDaRede_TrancaLivre() {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);

        Tranca trancaLivre = new Tranca();
        trancaLivre.setId(1L);
        trancaLivre.setNumerobicicleta(null); // Tranca está livre

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(trancaLivre));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });
    }

    @Test
    void testRetirarBicicletaDaRede_BicicletaNaoCorresponde() {
        // Arrange
        RetirarBicicletaDTO dto = new RetirarBicicletaDTO();
        dto.setIdTranca(1L);
        dto.setIdBicicleta(1L);
        
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(1L);
        bicicleta.setNumero(12345);

        Tranca trancaComOutraBicicleta = new Tranca();
        trancaComOutraBicicleta.setId(1L);
        trancaComOutraBicicleta.setBicicleta(99999); // Número diferente

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(trancaComOutraBicicleta));
        when(bicicletaRepository.findById(1L)).thenReturn(Optional.of(bicicleta));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            bicicletaService.retirarBicicletaDaRede(dto);
        });
    }
}
