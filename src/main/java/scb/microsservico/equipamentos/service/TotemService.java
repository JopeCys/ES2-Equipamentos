package scb.microsservico.equipamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.mapper.TotemMapper;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.repository.TotemRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class TotemService {
    private final TotemRepository totemRepository;  // Repositório para acesso ao banco

    // Cria um novo totem
    public void criarTotem(TotemCreateDTO dto) {
        Totem totem = TotemMapper.toEntity(dto);
        totemRepository.save(totem);
    }

    // Busca um totem pelo ID
    public TotemResponseDTO buscarTotemPorId(Long idTotem) {
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(TotemNotFoundException::new);
        return TotemMapper.toDTO(totem);
    }

    // Retorna todos os totens cadastrados
    public List<TotemResponseDTO> buscarTodosTotens() {
        return totemRepository.findAll()
                .stream()
                .map(TotemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Atualiza dados de um totem existente
    public TotemResponseDTO atualizarTotem(Long idTotem, TotemUpdateDTO dto) {
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(TotemNotFoundException::new);

        totem.setLocalizacao(dto.getLocalizacao());
        totem.setDescricao(dto.getDescricao());

        totemRepository.save(totem);
        return TotemMapper.toDTO(totem);
    }

    // Deleta completamente um totem
    public void deletarTotem(Long idTotem) {
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(TotemNotFoundException::new);
        totemRepository.delete(totem);
    }
}