package scb.microsservico.equipamentos.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class AdminService {

    private final BicicletaRepository bicicletaRepository;
    private final TrancaRepository trancaRepository;
    private final TotemRepository totemRepository;
    private final RegistroOperacaoRepository registroOperacaoRepository;

    public void restaurarBanco() {
        // Apaga todos os registros das tabelas de equipamentos
        bicicletaRepository.deleteAll();
        trancaRepository.deleteAll();
        totemRepository.deleteAll();
        registroOperacaoRepository.deleteAll();
    }

    public void restaurarDados() {
        // 1. Restaurar o banco de dados
        restaurarBanco();

        // 2. Criar Totem
        Totem totem1 = new Totem();
        totem1.setId(1L);
        totem1.setLocalizacao("Rio de Janeiro");
        totem1.setDescricao("Descrição");
        totemRepository.save(totem1);

        // 3. Criar Bicicletas

        // --- Bicicleta 1 ---
        Bicicleta bicicleta1 = new Bicicleta();
        bicicleta1.setId(1L); // Identificador
        bicicleta1.setMarca("Caloi");
        bicicleta1.setModelo("Caloi");
        bicicleta1.setAno("2020");
        bicicleta1.setNumero(12345);
        bicicleta1.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta1);

        // --- Bicicleta 2 ---
        Bicicleta bicicleta2 = new Bicicleta();
        bicicleta2.setId(2L); // Identificador
        bicicleta2.setMarca("Caloi");
        bicicleta2.setModelo("Caloi");
        bicicleta2.setAno("2020");
        bicicleta2.setNumero(12345);
        bicicleta2.setStatus(BicicletaStatus.REPARO_SOLICITADO);
        bicicletaRepository.save(bicicleta2);

        // --- Bicicleta 3 ---
        Bicicleta bicicleta3 = new Bicicleta();
        bicicleta3.setId(3L); // Identificador
        bicicleta3.setMarca("Caloi");
        bicicleta3.setModelo("Caloi");
        bicicleta3.setAno("2020");
        bicicleta3.setNumero(12345);
        bicicleta3.setStatus(BicicletaStatus.EM_USO);
        bicicletaRepository.save(bicicleta3);

        // --- Bicicleta 4 ---
        Bicicleta bicicleta4 = new Bicicleta();
        bicicleta4.setId(4L); // Identificador
        bicicleta4.setMarca("Caloi");
        bicicleta4.setModelo("Caloi");
        bicicleta4.setAno("2020");
        bicicleta4.setNumero(12345);
        bicicleta4.setStatus(BicicletaStatus.EM_REPARO);
        bicicletaRepository.save(bicicleta4);

        // --- Bicicleta 5 ---
        Bicicleta bicicleta5 = new Bicicleta();
        bicicleta5.setId(5L); // Identificador
        bicicleta5.setMarca("Caloi");
        bicicleta5.setModelo("Caloi");
        bicicleta5.setAno("2020");
        bicicleta5.setNumero(12345);
        bicicleta5.setStatus(BicicletaStatus.EM_USO);
        bicicletaRepository.save(bicicleta5);

        // 4. Criar Trancas

        // --- Tranca 1 ---
        Tranca tranca1 = new Tranca();
        tranca1.setId(1L);
        tranca1.setNumero(12345);
        tranca1.setAnoDeFabricacao("2020");
        tranca1.setModelo("Caloi");
        tranca1.setLocalizacao("Rio de Janeiro");
        tranca1.setStatus(TrancaStatus.OCUPADA);
        tranca1.setBicicleta(bicicleta1.getNumero()); // Associando o NÚMERO da bicicleta 1
        tranca1.setTotem(totem1);
        trancaRepository.save(tranca1);

        // --- Tranca 2 ---
        Tranca tranca2 = new Tranca();
        tranca2.setId(2L);
        tranca2.setNumero(12345);
        tranca2.setAnoDeFabricacao("2020");
        tranca2.setModelo("Caloi");
        tranca2.setLocalizacao("Rio de Janeiro");
        tranca2.setStatus(TrancaStatus.LIVRE);
        // Nenhuma bicicleta associada, o campo ficará nulo
        tranca2.setTotem(totem1);
        trancaRepository.save(tranca2);

        // --- Tranca 3 ---
        Tranca tranca3 = new Tranca();
        tranca3.setId(3L);
        tranca3.setNumero(12345);
        tranca3.setAnoDeFabricacao("2020");
        tranca3.setModelo("Caloi");
        tranca3.setLocalizacao("Rio de Janeiro");
        tranca3.setStatus(TrancaStatus.OCUPADA);
        tranca3.setBicicleta(bicicleta2.getNumero()); // Associando o NÚMERO/ID da bicicleta 2
        tranca3.setTotem(totem1);
        trancaRepository.save(tranca3);

        // --- Tranca 4 ---
        Tranca tranca4 = new Tranca();
        tranca4.setId(4L);
        tranca4.setNumero(12345);
        tranca4.setAnoDeFabricacao("2020");
        tranca4.setModelo("Caloi");
        tranca4.setLocalizacao("Rio de Janeiro");
        tranca4.setStatus(TrancaStatus.OCUPADA);
        tranca4.setBicicleta(bicicleta5.getNumero()); // Associando o NÚMERO/ID da bicicleta 5
        tranca4.setTotem(totem1);
        trancaRepository.save(tranca4);

        // --- Tranca 5 ---
        Tranca tranca5 = new Tranca();
        tranca5.setId(5L);
        tranca5.setNumero(12345);
        tranca5.setAnoDeFabricacao("2020");
        tranca5.setModelo("Caloi");
        tranca5.setLocalizacao("Rio de Janeiro");
        tranca5.setStatus(TrancaStatus.EM_REPARO);
        // Nenhuma bicicleta associada, o campo ficará nulo
        tranca5.setTotem(totem1);
        trancaRepository.save(tranca5);

        // --- Tranca 6 ---
        Tranca tranca6 = new Tranca();
        tranca6.setId(6L);
        tranca6.setNumero(12345);
        tranca6.setAnoDeFabricacao("2020");
        tranca6.setModelo("Caloi");
        tranca6.setLocalizacao("Rio de Janeiro");
        tranca6.setStatus(TrancaStatus.EM_REPARO);
        // Nenhuma bicicleta associada, o campo ficará nulo
        tranca6.setTotem(totem1);
        trancaRepository.save(tranca6);
    }
}