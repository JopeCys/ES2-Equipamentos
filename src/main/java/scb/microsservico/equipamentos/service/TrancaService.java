package scb.microsservico.equipamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.TrancaRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class TrancaService {
    private final TrancaRepository trancaRepository; // Repositório para acesso ao banco

    // Cria uma nova tranca a partir do DTO
    public void criarTranca(TrancaCreateDTO dto) {
        Tranca tranca = TrancaMapper.toEntity(dto);
        trancaRepository.save(tranca);
    }

    // Busca uma tranca pelo ID, lança exceção se não encontrar
    public TrancaResponseDTO buscarTrancaPorId(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);
        return TrancaMapper.toDTO(tranca);
    }

    // Retorna todas as trancas cadastradas
    public List<TrancaResponseDTO> buscarTodasTrancas() {
        return trancaRepository.findAll()
                .stream()
                .map(TrancaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Atualiza dados de uma tranca existente
    public TrancaResponseDTO atualizarTranca(Long idTranca, TrancaUpdateDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        tranca.setLocalizacao(dto.getLocalizacao());
        tranca.setAnoDeFabricacao(dto.getAnoDeFabricacao());
        tranca.setModelo(dto.getModelo());

        trancaRepository.save(tranca);
        return TrancaMapper.toDTO(tranca);
    }

    // Faz o soft delete de uma tranca, marcando como APOSENTADA, exceto se estiver OCUPADA
    public void deletarTranca(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);
        if (TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            throw new TrancaOcupadaException();
        }
        tranca.setStatus(TrancaStatus.APOSENTADA);
        trancaRepository.save(tranca);
    }
}