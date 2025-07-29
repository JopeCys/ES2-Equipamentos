package scb.microsservico.equipamentos.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TotemIntegrationTest {
    
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TotemRepository totemRepository;

    @Autowired
    private TrancaRepository trancaRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Test
    public void criarTotem_ComDadosValidos_DeveRetornarAccepted() throws Exception {
        TotemCreateDTO createDTO = new TotemCreateDTO();
        createDTO.setLocalizacao("Praca da Se");
        createDTO.setDescricao("Totem em frente a catedral");

        mockMvc.perform(post("/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Totem Cadastrado"));
    }

    @Test
    public void buscarTotemPorId_ComIdExistente_DeveRetornarTotem() throws Exception {
        Totem totem = new Totem();
        totem.setLocalizacao("Parque Ibirapuera");
        totem.setDescricao("Totem proximo ao lago");
        totemRepository.save(totem);

        mockMvc.perform(get("/totem/{idTotem}", totem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localizacao", is("Parque Ibirapuera")))
                .andExpect(jsonPath("$.descricao", is("Totem proximo ao lago")));
    }

    @Test
    public void buscarTodosTotens_DeveRetornarListaDeTotens() throws Exception {
        Totem totem1 = new Totem();
        totem1.setLocalizacao("Avenida Paulista");
        totem1.setDescricao("Em frente ao MASP");
        totemRepository.save(totem1);

        Totem totem2 = new Totem();
        totem2.setLocalizacao("Rua Augusta");
        totem2.setDescricao("Esquina com a Rua Oscar Freire");
        totemRepository.save(totem2);

        mockMvc.perform(get("/totem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void atualizarTotem_ComDadosValidos_DeveRetornarTotemAtualizado() throws Exception {
        Totem totem = new Totem();
        totem.setLocalizacao("Localizacao Antiga");
        totem.setDescricao("Descricao Antiga");
        totemRepository.save(totem);

        TotemUpdateDTO updateDTO = new TotemUpdateDTO();
        updateDTO.setLocalizacao("Localizacao Nova");
        updateDTO.setDescricao("Descricao Nova");

        mockMvc.perform(put("/totem/{idTotem}", totem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localizacao", is("Localizacao Nova")))
                .andExpect(jsonPath("$.descricao", is("Descricao Nova")));
    }

    @Test
    public void deletarTotem_ComIdExistente_DeveRetornarAccepted() throws Exception {
        Totem totem = new Totem();
        totem.setLocalizacao("Totem a ser deletado");
        totem.setDescricao("Descricao qualquer");
        totemRepository.save(totem);

        mockMvc.perform(delete("/totem/{idTotem}", totem.getId()))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Totem Deletado"));
    }

   @Test
    public void listarTrancasDoTotem_ComTotemExistente_DeveRetornarListaDeTrancas() throws Exception {
        // Setup
        Totem totem = new Totem();
        totem.setLocalizacao("Central Park");
        totem.setDescricao("Entrada principal");
        totemRepository.save(totem);

        Tranca tranca1 = new Tranca();
        tranca1.setNumero(101);
        tranca1.setModelo("T-100");
        tranca1.setStatus(TrancaStatus.LIVRE);
        tranca1.setTotem(totem);
        tranca1.setAnoDeFabricacao("2023"); // FIX: Added missing required field
        trancaRepository.save(tranca1);
        
        Tranca tranca2 = new Tranca();
        tranca2.setNumero(102);
        tranca2.setModelo("T-200");
        tranca2.setStatus(TrancaStatus.OCUPADA);
        tranca2.setTotem(totem);
        tranca2.setAnoDeFabricacao("2024"); // FIX: Added missing required field
        trancaRepository.save(tranca2);
        
        entityManager.flush();
        entityManager.clear();

        // Ação e Verificação
        mockMvc.perform(get("/totem/{idTotem}/trancas", totem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void listarBicicletas_ComTotemETrancasComBicicletas_DeveRetornarListaDeBicicletas() throws Exception {

        Bicicleta bicicleta1 = new Bicicleta();
        bicicleta1.setNumero(901);
        bicicleta1.setMarca("Caloi");
        bicicleta1.setModelo("Explorer");
        bicicleta1.setAno("2023");
        bicicleta1.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta1);

        Bicicleta bicicleta2 = new Bicicleta();
        bicicleta2.setNumero(902);
        bicicleta2.setMarca("Monark");
        bicicleta2.setModelo("BMX");
        bicicleta2.setAno("2022");
        bicicleta2.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta2);

        Totem totem = new Totem();
        totem.setLocalizacao("Largo da Batata");
        totem.setDescricao("Proximo ao metro Faria Lima");
        totemRepository.save(totem);
        
        Tranca tranca1 = new Tranca();
        tranca1.setNumero(201);
        tranca1.setModelo("T-300");
        tranca1.setStatus(TrancaStatus.OCUPADA);
        tranca1.setBicicleta(bicicleta1.getNumero());
        tranca1.setTotem(totem);
        tranca1.setAnoDeFabricacao("2024"); // FIX: Added missing required field
        trancaRepository.save(tranca1);

        Tranca tranca2 = new Tranca();
        tranca2.setNumero(202);
        tranca2.setModelo("T-300");
        tranca2.setStatus(TrancaStatus.OCUPADA);
        tranca2.setBicicleta(bicicleta2.getNumero());
        tranca2.setTotem(totem);
        tranca2.setAnoDeFabricacao("2024"); // FIX: Added missing required field
        trancaRepository.save(tranca2);
        
        entityManager.flush();
        entityManager.clear();
        
        // Ação e Verificação
        mockMvc.perform(get("/totem/{idTotem}/bicicletas", totem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}