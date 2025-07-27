package scb.microsservico.equipamentos.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import scb.microsservico.equipamentos.model.RegistroOperacao;
import scb.microsservico.equipamentos.repository.RegistroOperacaoRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class OperacaoService {
    private final RegistroOperacaoRepository registroOperacaoRepository;

    public void registrarOperacao(String tipo, String descricao, Long idFuncionario) {
        RegistroOperacao registro = new RegistroOperacao();
        registro.setTipo(tipo);
        registro.setDescricao(descricao);
        registro.setDataHora(java.time.LocalDateTime.now());
        registro.setIdFuncionario(idFuncionario);
        registroOperacaoRepository.save(registro);
    }
}
