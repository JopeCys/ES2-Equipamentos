package scb.microsservico.equipamentos.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Client.FuncionarioEmailDTO;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;

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
public class BicicletaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private TrancaRepository trancaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AluguelServiceClient aluguelServiceClient;

    @MockBean
    private ExternoServiceClient externoServiceClient;

    @Test
    public void criarBicicleta_ComDadosValidos_DeveRetornarAccepted() throws Exception {
        BicicletaCreateDTO createDTO = new BicicletaCreateDTO();
        createDTO.setMarca("Caloi");
        createDTO.setModelo("Ceci");
        createDTO.setAno("2022");

        mockMvc.perform(post("/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Bicicleta Cadastrada"));
    }

    @Test
    public void buscarBicicletaPorId_ComIdExistente_DeveRetornarBicicleta() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Monark");
        bicicleta.setModelo("Barra Circular");
        bicicleta.setAno("1990");
        bicicleta.setNumero(123);
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta);

        mockMvc.perform(get("/bicicleta/{idBicicleta}", bicicleta.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca", is("Monark")))
                .andExpect(jsonPath("$.modelo", is("Barra Circular")));
    }

    @Test
    public void buscarTodasBicicletas_DeveRetornarListaDeBicicletas() throws Exception {
        Bicicleta bicicleta1 = new Bicicleta();
        bicicleta1.setMarca("Caloi");
        bicicleta1.setModelo("Ceci");
        bicicleta1.setAno("2022");
        bicicleta1.setNumero(100);
        bicicleta1.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta1);
        
        Bicicleta bicicleta2 = new Bicicleta();
        bicicleta2.setMarca("Monark");
        bicicleta2.setModelo("BMX");
        bicicleta2.setAno("1995");
        bicicleta2.setNumero(101);
        bicicleta2.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta2);

        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void atualizarBicicleta_ComDadosValidos_DeveRetornarBicicletaAtualizada() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Marca Antiga");
        bicicleta.setModelo("Modelo Antigo");
        bicicleta.setAno("2000");
        bicicleta.setNumero(200);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta);

        BicicletaUpdateDTO updateDTO = new BicicletaUpdateDTO();
        updateDTO.setMarca("Marca Nova");
        updateDTO.setModelo("Modelo Novo");
        updateDTO.setAno("2023");

        mockMvc.perform(put("/bicicleta/{idBicicleta}", bicicleta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca", is("Marca Nova")))
                .andExpect(jsonPath("$.modelo", is("Modelo Novo")));
    }

    @Test
    public void deletarBicicleta_ComStatusValido_DeveRetornarAccepted() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Marca");
        bicicleta.setModelo("Modelo");
        bicicleta.setAno("2021");
        bicicleta.setNumero(300);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta);

        mockMvc.perform(delete("/bicicleta/{idBicicleta}", bicicleta.getId()))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Bicicleta Deletada"));
    }

    @Test
    public void alterarStatusBicicleta_ComStatusValido_DeveRetornarOk() throws Exception {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Marca");
        bicicleta.setModelo("Modelo");
        bicicleta.setAno("2022");
        bicicleta.setNumero(400);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta);

        mockMvc.perform(post("/bicicleta/{idBicicleta}/status/{acao}", bicicleta.getId(), "EM_REPARO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("EM_REPARO")));
    }
    
   @Test
    public void integrarBicicletaNaRede_ComDadosValidos_DeveRetornarAcceptedEChamarServicosExternos() throws Exception {
        // Setup
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Trek");
        bicicleta.setModelo("Marlin 5");
        bicicleta.setAno("2023");
        bicicleta.setNumero(500);
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicletaRepository.save(bicicleta);

        Tranca tranca = new Tranca();
        tranca.setNumero(1);
        tranca.setLocalizacao("Rua das Flores");
        tranca.setModelo("T1000");
        tranca.setAnoDeFabricacao("2023");
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);

        Long idFuncionario = 10L;

        IntegrarBicicletaDTO integrarDTO = new IntegrarBicicletaDTO();
        integrarDTO.setIdBicicleta(bicicleta.getId());
        integrarDTO.setIdTranca(tranca.getId());
        integrarDTO.setIdFuncionario(idFuncionario);

        // Configura o comportamento dos mocks do Feign
        FuncionarioEmailDTO emailDTO = new FuncionarioEmailDTO();
        emailDTO.setEmail("funcionario@teste.com");
        when(aluguelServiceClient.getEmailFuncionario(idFuncionario)).thenReturn(emailDTO);
        doNothing().when(externoServiceClient).enviarEmail(any());

        // Ação e Verificação
        mockMvc.perform(post("/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrarDTO)))
                .andExpect(status().isAccepted()) // Espera 202 Accepted
                .andExpect(content().string("Bicicleta Integrada"));
    }

    @Test
    public void retirarBicicletaDaRede_ComDadosValidos_DeveRetornarAcceptedEChamarServicosExternos() throws Exception {
        // Setup
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Specialized");
        bicicleta.setModelo("Rockhopper");
        bicicleta.setAno("2022");
        bicicleta.setNumero(600);
        bicicleta.setStatus(BicicletaStatus.EM_REPARO);
        bicicletaRepository.save(bicicleta);

        Tranca tranca = new Tranca();
        tranca.setNumero(2);
        tranca.setLocalizacao("Avenida Principal");
        tranca.setModelo("T2000");
        tranca.setAnoDeFabricacao("2022");
        tranca.setBicicleta(bicicleta.getNumero());
        tranca.setStatus(TrancaStatus.OCUPADA);
        trancaRepository.save(tranca);

        Long idFuncionario = 12L;

        RetirarBicicletaDTO retirarDTO = new RetirarBicicletaDTO();
        retirarDTO.setIdBicicleta(bicicleta.getId());
        retirarDTO.setIdTranca(tranca.getId());
        retirarDTO.setIdFuncionario(idFuncionario);
        retirarDTO.setStatusAcaoReparador(AcaoRetirar.EM_REPARO);

        // Configura o comportamento dos mocks do Feign
        FuncionarioEmailDTO emailDTO = new FuncionarioEmailDTO();
        emailDTO.setEmail("reparador@teste.com");
        when(aluguelServiceClient.getEmailFuncionario(idFuncionario)).thenReturn(emailDTO);
        doNothing().when(externoServiceClient).enviarEmail(any());

        // Ação e Verificação
        mockMvc.perform(post("/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarDTO)))
                .andExpect(status().isAccepted()) // Espera 202 Accepted
                .andExpect(content().string("Bicicleta Retirada"));
    }
}