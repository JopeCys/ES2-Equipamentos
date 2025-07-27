package scb.microsservico.equipamentos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.AdminService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

public class AdminServiceTest {

    private BicicletaRepository bicicletaRepository;
    private TrancaRepository trancaRepository;
    private TotemRepository totemRepository;
    private RegistroOperacaoRepository registroOperacaoRepository;
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        bicicletaRepository = mock(BicicletaRepository.class);
        trancaRepository = mock(TrancaRepository.class);
        totemRepository = mock(TotemRepository.class);
        registroOperacaoRepository = mock(RegistroOperacaoRepository.class);
        adminService = new AdminService(bicicletaRepository, trancaRepository, totemRepository, registroOperacaoRepository);
    }

    @Test
    public void testRestaurarBanco_deletesAllFromRepositories() {
        adminService.restaurarBanco();

        verify(bicicletaRepository, times(1)).deleteAll();
        verify(trancaRepository, times(1)).deleteAll();
        verify(totemRepository, times(1)).deleteAll();
        verify(registroOperacaoRepository, times(1)).deleteAll();
    }

    @Test
    public void testRestaurarDados_savesAllEntities() {
        // Usa um spy para permitir a verificação da chamada a outro método na mesma classe
        AdminService adminServiceSpy = spy(adminService);
        
        // Impede a execução real de restaurarBanco para focar apenas em restaurarDados
        doNothing().when(adminServiceSpy).restaurarBanco();

        // Executa o método a ser testado
        adminServiceSpy.restaurarDados();

        // 1. Verifica se restaurarBanco() foi chamado primeiro
        verify(adminServiceSpy, times(1)).restaurarBanco();

        // 2. Verifica a criação do Totem
        ArgumentCaptor<Totem> totemCaptor = ArgumentCaptor.forClass(Totem.class);
        verify(totemRepository, times(1)).save(totemCaptor.capture());
        assertEquals("Rio de Janeiro", totemCaptor.getValue().getLocalizacao());

        // 3. Verifica a criação das Bicicletas
        ArgumentCaptor<Bicicleta> bicicletaCaptor = ArgumentCaptor.forClass(Bicicleta.class);
        verify(bicicletaRepository, times(5)).save(bicicletaCaptor.capture());
        List<Bicicleta> bicicletasSalvas = bicicletaCaptor.getAllValues();
        assertEquals(5, bicicletasSalvas.size());
        assertEquals(BicicletaStatus.DISPONIVEL, bicicletasSalvas.get(0).getStatus());
        assertEquals(BicicletaStatus.REPARO_SOLICITADO, bicicletasSalvas.get(1).getStatus());
        assertEquals(BicicletaStatus.EM_USO, bicicletasSalvas.get(2).getStatus());
        assertEquals(BicicletaStatus.EM_REPARO, bicicletasSalvas.get(3).getStatus());
        assertEquals(BicicletaStatus.EM_USO, bicicletasSalvas.get(4).getStatus());


        // 4. Verifica a criação das Trancas
        ArgumentCaptor<Tranca> trancaCaptor = ArgumentCaptor.forClass(Tranca.class);
        verify(trancaRepository, times(6)).save(trancaCaptor.capture());
        List<Tranca> trancasSalvas = trancaCaptor.getAllValues();
        assertEquals(6, trancasSalvas.size());
        // Verifica o status e a bicicleta associada (pelo número)
        assertEquals(TrancaStatus.OCUPADA, trancasSalvas.get(0).getStatus());
        assertEquals(bicicletasSalvas.get(0).getNumero(), trancasSalvas.get(0).getBicicleta());

        assertEquals(TrancaStatus.LIVRE, trancasSalvas.get(1).getStatus());

        assertEquals(TrancaStatus.OCUPADA, trancasSalvas.get(2).getStatus());
        assertEquals(bicicletasSalvas.get(1).getNumero(), trancasSalvas.get(2).getBicicleta());
        
        assertEquals(TrancaStatus.OCUPADA, trancasSalvas.get(3).getStatus());
        assertEquals(bicicletasSalvas.get(4).getNumero(), trancasSalvas.get(3).getBicicleta());

        assertEquals(TrancaStatus.EM_REPARO, trancasSalvas.get(4).getStatus());
        assertEquals(TrancaStatus.EM_REPARO, trancasSalvas.get(5).getStatus());
    }
}
