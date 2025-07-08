package scb.microsservico.equipamentos.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.mapper.TotemMapper;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class TotemService {
    private final TotemRepository totemRepository;
    private final BicicletaRepository bicicletaRepository;  // Repositório para acesso ao banco

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

    // Lista trancas e já converte para DTO
    public List<TrancaResponseDTO> listarTrancasPorTotem(Long idTotem) {
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(TotemNotFoundException::new);

        // Converte a lista de entidades Tranca para uma lista de TrancaResponseDTO
        return totem.getTrancas().stream()
                .map(TrancaMapper::toDTO) // ou .map(tranca -> TrancaMapper.toDTO(tranca))
                .collect(Collectors.toList());
    }

     public List<BicicletaResponseDTO> listarBicicletasDoTotem(Long idTotem) {
        // 1. Busca o totem pelo ID. Se não encontrar, lança uma exceção.
        Totem totem = totemRepository.findById(idTotem)
                .orElseThrow(() -> new EntityNotFoundException("Totem não encontrado com o id: " + idTotem));

        // 2. Extrai a lista de números de bicicleta das trancas.
        List<Integer> numerosDasBicicletas = totem.getTrancas()
                .stream()
                .map(Tranca::getBicicleta) // Pega o número da bicicleta de cada tranca
                .filter(Objects::nonNull)        // Ignora trancas que não têm uma bicicleta associada
                .collect(Collectors.toList());

        // 3. Para cada número, busca a entidade Bicicleta no banco de dados.
        List<Bicicleta> bicicletasEncontradas = numerosDasBicicletas.stream()
                .map(numero -> bicicletaRepository.findByNumero(numero)) // Busca cada bicicleta
                .filter(Optional::isPresent)  // Filtra os resultados para manter apenas as que foram encontradas
                .map(Optional::get)           // Extrai o objeto Bicicleta do Optional
                .collect(Collectors.toList());

        // 4. Converte a lista de entidades Bicicleta para uma lista de DTOs para a resposta.
        return bicicletasEncontradas.stream()
                .map(BicicletaMapper::toDTO)
                .collect(Collectors.toList());
    }
}