package scb.microsservico.equipamentos.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
}