package scb.microsservico.equipamentos.Tranca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.service.TrancaService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrancaServiceTest {

    // Mocks para as dependências do serviço
    private TrancaRepository trancaRepository;
    private BicicletaRepository bicicletaRepository;
    private TotemRepository totemRepository;

    // A instância do serviço que vamos testar
    private TrancaService trancaService;

    @BeforeEach
    void setUp() {
        // 1. Criamos os mocks para cada repositório usando Mockito.
        trancaRepository = Mockito.mock(TrancaRepository.class);
        bicicletaRepository = Mockito.mock(BicicletaRepository.class);
        totemRepository = Mockito.mock(TotemRepository.class);

        // 2. Instanciamos o serviço manualmente, injetando os mocks no construtor.
        //    Isso funciona por causa da anotação @RequiredArgsConstructor no serviço.
        trancaService = new TrancaService(trancaRepository, bicicletaRepository, totemRepository);
    }
    
    @Test
    @DisplayName("Deve criar uma nova tranca com status NOVA")
    void criarTranca_DeveSalvarComStatusNova() {
        // ARRANGE (Organizar)
        TrancaCreateDTO createDTO = new TrancaCreateDTO();
        createDTO.setNumero(101);
        createDTO.setModelo("Modelo Teste");
        createDTO.setAnoDeFabricacao("2024");
        
        // ArgumentCaptor para capturar o objeto Tranca que é passado para o método save
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);

        // ACT (Agir)
        trancaService.criarTranca(createDTO);

        // ASSERT (Afirmar)
        // Verifica se o método save do repositório foi chamado exatamente 1 vez.
        verify(trancaRepository, times(1)).save(trancaCaptor.capture());
        
        // Pega o objeto Tranca capturado
        Tranca trancaSalva = trancaCaptor.getValue();
        
        // Verifica se os dados e o status estão corretos
        assertEquals("Modelo Teste", trancaSalva.getModelo());
        assertEquals(TrancaStatus.NOVA, trancaSalva.getStatus());
    }

    @Test
    @DisplayName("Deve buscar tranca por ID e retornar DTO quando encontrada")
    void buscarTrancaPorId_QuandoEncontrada_DeveRetornarDTO() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setModelo("Modelo Existente");
        
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        
        // ACT
        var response = trancaService.buscarTrancaPorId(1L);
        
        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Modelo Existente", response.getModelo());
    }
    
    @Test
    @DisplayName("Deve lançar TrancaNotFoundException ao buscar por ID inexistente")
    void buscarTrancaPorId_QuandoNaoEncontrada_DeveLancarExcecao() {
        // ARRANGE
        when(trancaRepository.findById(99L)).thenReturn(Optional.empty());
        
        // ACT & ASSERT
        assertThrows(TrancaNotFoundException.class, () -> {
            trancaService.buscarTrancaPorId(99L);
        });
    }

    @Test
    @DisplayName("Deve deletar (aposentar) uma tranca que não está ocupada")
    void deletarTranca_QuandoLivre_DeveAlterarStatusParaAposentada() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.LIVRE);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        ArgumentCaptor<Tranca> captor = ArgumentCaptor.forClass(Tranca.class);

        // ACT
        trancaService.deletarTranca(1L);

        // ASSERT
        verify(trancaRepository, times(1)).save(captor.capture());
        assertEquals(TrancaStatus.APOSENTADA, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("Deve lançar TrancaOcupadaException ao tentar deletar tranca ocupada")
    void deletarTranca_QuandoOcupada_DeveLancarExcecao() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.OCUPADA);
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));

        // ACT & ASSERT
        assertThrows(TrancaOcupadaException.class, () -> {
            trancaService.deletarTranca(1L);
        });

        // Garante que o método save nunca foi chamado
        verify(trancaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve trancar uma tranca livre com uma bicicleta válida")
    void trancarTranca_ComTrancaLivreEBicicletaValida_DeveFuncionar() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.LIVRE);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(10L);
        bicicleta.setNumero(12345);
        bicicleta.setStatus(BicicletaStatus.EM_USO);

        TrancarRequestDTO requestDTO = new TrancarRequestDTO();
        requestDTO.setIdBicicleta(10L);

        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(10L)).thenReturn(Optional.of(bicicleta));
        
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);

        // ACT
        trancaService.trancarTranca(1L, requestDTO);

        // ASSERT
        verify(trancaRepository).save(trancaCaptor.capture());
        verify(bicicletaRepository).save(bicicletaCaptor.capture());
        
        assertEquals(TrancaStatus.OCUPADA, trancaCaptor.getValue().getStatus());
        assertEquals(12345, trancaCaptor.getValue().getBicicleta());
        assertEquals(BicicletaStatus.DISPONIVEL, bicicletaCaptor.getValue().getStatus());
    }
    
    @Test
    @DisplayName("Deve lançar BicicletaNotFoundException ao trancar com bicicleta inexistente")
    void trancarTranca_ComBicicletaInexistente_DeveLancarExcecao() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setStatus(TrancaStatus.LIVRE);
        
        TrancarRequestDTO requestDTO = new TrancarRequestDTO();
        requestDTO.setIdBicicleta(99L);
        
        when(trancaRepository.findById(1L)).thenReturn(Optional.of(tranca));
        // Simula que a bicicleta não foi encontrada
        when(bicicletaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(BicicletaNotFoundException.class, () -> {
            trancaService.trancarTranca(1L, requestDTO);
        });
    }
}