package scb.microsservico.equipamentos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.service.AdminService;
import static org.mockito.Mockito.*;

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
}
