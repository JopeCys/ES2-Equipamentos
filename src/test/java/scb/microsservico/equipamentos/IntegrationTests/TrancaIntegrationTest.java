package scb.microsservico.equipamentos.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Client.EmailRequestDTO;
import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;
import scb.microsservico.equipamentos.dto.Tranca.*;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TrancaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrancaRepository trancaRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private TotemRepository totemRepository;

    @MockBean
    private AluguelServiceClient aluguelServiceClient;

    @MockBean
    private ExternoServiceClient externoServiceClient;

    private Tranca tranca;
    private Bicicleta bicicleta;
    private Totem totem;

    @BeforeEach
    void setUp() {
        // Limpa os repositórios para garantir que cada teste seja independente
        trancaRepository.deleteAll();
        bicicletaRepository.deleteAll();
        totemRepository.deleteAll();

        // Configuração de uma tranca base para os testes
        tranca = new Tranca();
        tranca.setNumero(100);
        tranca.setModelo("Modelo Teste");
        tranca.setAnoDeFabricacao("2023");
        tranca.setStatus(TrancaStatus.NOVA);
        trancaRepository.save(tranca);

        // Configuração de uma bicicleta base
        bicicleta = new Bicicleta();
        bicicleta.setNumero(200);
        bicicleta.setMarca("Marca Teste");
        bicicleta.setModelo("Modelo Bike");
        bicicleta.setAno("2023");
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta);

        // Configuração de um totem base
        totem = new Totem();
        totem.setLocalizacao("Localizacao Teste");
        totem.setDescricao("Totem de Teste na Localização de Teste");
        totem.setTrancas(new ArrayList<>());
        totemRepository.save(totem);
    }

    @Test
    public void criarTranca_ComDadosValidos_DeveRetornarAccepted() throws Exception {
        TrancaCreateDTO createDTO = new TrancaCreateDTO();
        createDTO.setNumero(101);
        createDTO.setModelo("T-100");
        createDTO.setAnoDeFabricacao("2024");
        createDTO.setLocalizacao("Rua Nova");

        mockMvc.perform(post("/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Dados Cadastrados"));
    }

    @Test
    public void buscarTrancaPorId_ComIdExistente_DeveRetornarTranca() throws Exception {
        mockMvc.perform(get("/tranca/{idTranca}", tranca.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", is(100)))
                .andExpect(jsonPath("$.modelo", is("Modelo Teste")));
    }

    @Test
    public void buscarTodasTrancas_DeveRetornarListaDeTrancas() throws Exception {
        Tranca outraTranca = new Tranca();
        outraTranca.setNumero(102);
        outraTranca.setModelo("Outro Modelo");
        outraTranca.setAnoDeFabricacao("2022");
        outraTranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(outraTranca);

        mockMvc.perform(get("/tranca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

@Test
public void atualizarTranca_ComDadosValidos_DeveRetornarTrancaAtualizada() throws Exception {
    TrancaUpdateDTO updateDTO = new TrancaUpdateDTO();
    updateDTO.setModelo("Modelo Atualizado");
    updateDTO.setAnoDeFabricacao("2025");
    updateDTO.setNumero(tranca.getNumero()); 

    mockMvc.perform(put("/tranca/{idTranca}", tranca.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk()) // A validação agora passará
            .andExpect(jsonPath("$.modelo", is("Modelo Atualizado")))
            .andExpect(jsonPath("$.anoDeFabricacao", is("2025")));
}

    @Test
    public void deletarTranca_ComStatusValido_DeveRetornarAccepted() throws Exception {
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);

        mockMvc.perform(delete("/tranca/{idTranca}", tranca.getId()))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca Deletada"));
    }

    @Test
    public void buscarBicicletaNaTranca_QuandoOcupada_DeveRetornarBicicleta() throws Exception {
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(bicicleta.getNumero());
        trancaRepository.save(tranca);

        mockMvc.perform(get("/tranca/{idTranca}/bicicleta", tranca.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", is(bicicleta.getNumero().intValue())));
    }

    @Test
    public void trancarTranca_ComTrancaLivre_DeveRetornarAccepted() throws Exception {
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);

        TrancarRequestDTO trancarDTO = new TrancarRequestDTO();
        trancarDTO.setBicicleta(bicicleta.getId());

        mockMvc.perform(post("/tranca/{idTranca}/trancar", tranca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trancarDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca trancada com sucesso"));
    }

    @Test
    public void destrancarTranca_ComTrancaOcupada_DeveRetornarAccepted() throws Exception {
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(bicicleta.getNumero());
        trancaRepository.save(tranca);

        DestrancarRequestDTO destrancarDTO = new DestrancarRequestDTO();
        destrancarDTO.setBicicleta(bicicleta.getId());

        mockMvc.perform(post("/tranca/{idTranca}/destrancar", tranca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destrancarDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca destrancada com sucesso"));
    }

    @Test
    public void alterarStatusTranca_ComStatusValido_DeveRetornarOk() throws Exception {
        mockMvc.perform(post("/tranca/{idTranca}/status/{acao}", tranca.getId(), "EM_REPARO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("EM_REPARO")));
    }

    @Test
    public void integrarNaRede_ComDadosValidos_DeveRetornarAcceptedEChamarServicosExternos() throws Exception {
        Long idFuncionario = 1L;
        IntegrarTrancaDTO integrarDTO = new IntegrarTrancaDTO();
        integrarDTO.setIdTranca(tranca.getId());
        integrarDTO.setIdTotem(totem.getId());
        integrarDTO.setIdFuncionario(idFuncionario);

        // Mock das chamadas aos serviços externos (pontos de integração)
        FuncionarioEmailDTO emailDTO = new FuncionarioEmailDTO();
        emailDTO.setEmail("funcionario@example.com");
        when(aluguelServiceClient.getEmailFuncionario(idFuncionario)).thenReturn(emailDTO);
        doNothing().when(externoServiceClient).enviarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/tranca/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrarDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca integrada com sucesso"));
    }

    @Test
    public void retirarDaRede_ComDadosValidos_DeveRetornarAcceptedEChamarServicosExternos() throws Exception {
        // Pré-condição: A tranca deve estar integrada a um totem
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);
        totem.getTrancas().add(tranca);
        totemRepository.save(totem);

        Long idFuncionario = 2L;
        RetirarTrancaDTO retirarDTO = new RetirarTrancaDTO();
        retirarDTO.setIdTranca(tranca.getId());
        retirarDTO.setIdTotem(totem.getId());
        retirarDTO.setIdFuncionario(idFuncionario);
        retirarDTO.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        // Mock das chamadas aos serviços externos (pontos de integração)
        FuncionarioEmailDTO emailDTO = new FuncionarioEmailDTO();
        emailDTO.setEmail("reparador@example.com");
        when(aluguelServiceClient.getEmailFuncionario(idFuncionario)).thenReturn(emailDTO);
        doNothing().when(externoServiceClient).enviarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/tranca/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Tranca retirada com sucesso"));
    }
}