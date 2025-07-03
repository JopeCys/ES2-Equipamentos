package scb.microsservico.equipamentos.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.repository.BicicletaRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class BicicletaService {
    private final BicicletaRepository bicicletaRepository; // Repositório para acesso ao banco

    // Cria uma nova bicicleta a partir do DTO
    public void criarBicicleta(BicicletaCreateDTO dto) {
        Bicicleta bicicleta = BicicletaMapper.toEntity(dto);
        bicicleta.setStatus(BicicletaStatus.NOVA);
        bicicleta.setNumero(new Random().nextInt(1000000)); // Número aleatório de 0 a 999999
        bicicletaRepository.save(bicicleta);
    }

    // Busca uma bicicleta pelo ID
    public BicicletaResponseDTO buscarBicicletaPorId(Long idBicicleta) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);
        return BicicletaMapper.toDTO(bicicleta);
    }

    // Retorna todas as bicicletas cadastradas
    public List<BicicletaResponseDTO> buscarTodasBicicletas() {
        return bicicletaRepository.findAll()
                .stream()
                .map(BicicletaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Atualiza dados de uma bicicleta existente
    public BicicletaResponseDTO atualizarBicicleta(Long idBicicleta, BicicletaUpdateDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);

        bicicleta.setMarca(dto.getMarca());
        bicicleta.setModelo(dto.getModelo());
        bicicleta.setAno(dto.getAno());

        bicicletaRepository.save(bicicleta);
        return BicicletaMapper.toDTO(bicicleta);
    }

    // Faz o soft delete de uma bicicleta, marcando como APOSENTADA, exceto se estiver EM_USO
    public void deletarBicicleta(Long idBicicleta) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);
        if (BicicletaStatus.EM_USO.equals(bicicleta.getStatus())) {
            throw new BicicletaOcupadaException();
        }
        bicicleta.setStatus(BicicletaStatus.APOSENTADA);
        bicicletaRepository.save(bicicleta);
    }
}