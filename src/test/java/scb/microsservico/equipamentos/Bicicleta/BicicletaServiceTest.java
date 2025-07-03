package scb.microsservico.equipamentos.Bicicleta;

import static org.junit.jupiter.api.Assertions.*;
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
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.service.BicicletaService;


public class BicicletaServiceTest {

    @Mock
    private BicicletaRepository bicicletaRepository;

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
}
