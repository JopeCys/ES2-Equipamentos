package scb.microsservico.equipamentos.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class BicicletaService {
    private final BicicletaRepository bicicletaRepository; // Repositório para acesso ao banco
    private final TrancaRepository trancaRepository; // Repositório para acesso às trancas

    // Cria uma nova bicicleta a partir do DTO
    public void criarBicicleta(BicicletaCreateDTO dto) {
        Bicicleta bicicleta = BicicletaMapper.toEntity(dto);
        bicicleta.setStatus(BicicletaStatus.NOVA);

        int numero;
        SecureRandom secureRandom = new SecureRandom();
        do {
            numero = secureRandom.nextInt(1000000);
        } while (bicicletaRepository.existsByNumero(numero)); 
        
        bicicleta.setNumero(numero);
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

    public void alterarStatus(Long idBicicleta, BicicletaStatus novoStatus) {
        // Busca a bicicleta no banco de dados ou lança uma exceção se não encontrar.
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);

        // Define o novo status na entidade.
        bicicleta.setStatus(novoStatus);

        // Salva a entidade bicicleta com o status atualizado.
        bicicletaRepository.save(bicicleta);
    }

   @Transactional
    public void integrarBicicletaNaRede(IntegrarBicicletaDTO dto) {
        // BUSCAR E VALIDAR AS ENTIDADES
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new EntityNotFoundException("Tranca não encontrada com o ID: " + dto.getIdTranca()));

        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new EntityNotFoundException("Bicicleta não encontrada com o ID: " + dto.getIdBicicleta()));

        // Valida se a tranca já não está com uma bicicleta
        if (tranca.getBicicleta() != null || tranca.getStatus() == TrancaStatus.OCUPADA) {
            throw new IllegalStateException("A tranca " + tranca.getId() + " já está ocupada.");
        }

        // Valida o status da bicicleta (só pode integrar se for NOVA ou vindo de reparo)
        if (bicicleta.getStatus() != BicicletaStatus.NOVA && bicicleta.getStatus() != BicicletaStatus.EM_REPARO) {
            throw new IllegalStateException("A bicicleta " + bicicleta.getId() + " não está com status 'NOVA' ou 'EM_REPARO' e não pode ser integrada.");
        }

        // ATUALIZAR O ESTADO DAS ENTIDADES
        // CORREÇÃO 1: O status da tranca deve ser OCUPADA, não APOSENTADA.
        tranca.setStatus(TrancaStatus.OCUPADA);
        
        // CORREÇÃO 2: Usar o nome do método setter consistente com o getter.
        tranca.setBicicleta(bicicleta.getNumero()); // Relaciona a bicicleta à tranca

        // A bicicleta agora está disponível para uso na rede
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        // Lógica de log e funcionário...

        // PERSISTIR AS ALTERAÇÕES
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    @Transactional
    public void retirarBicicletaDaRede(RetirarBicicletaDTO dto) {
        // BUSCAR E VALIDAR AS ENTIDADES
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new EntityNotFoundException("Tranca não encontrada com o ID: " + dto.getIdTranca()));

        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new EntityNotFoundException("Bicicleta não encontrada com o ID: " + dto.getIdBicicleta()));

        // Valida se a tranca realmente contém uma bicicleta
        if (tranca.getBicicleta() == null) {
            throw new IllegalStateException("A tranca " + tranca.getId() + " já está livre.");
        }

        // Valida se a bicicleta a ser removida é a mesma que está na tranca
        if (!tranca.getBicicleta().equals(bicicleta.getNumero())) {
            throw new IllegalStateException("A bicicleta " + bicicleta.getId() + " não corresponde à bicicleta registrada na tranca " + tranca.getId() + ".");
        }

        // ATUALIZAR O ESTADO DAS ENTIDADES (Processo Inverso)
        
        // Libera a tranca, deixando-a disponível para outra bicicleta
        tranca.setBicicleta(null); // Remove a referência da bicicleta
        tranca.setStatus(TrancaStatus.LIVRE); // Define o status como livre/disponível

        // Altera o status da bicicleta para indicar que está em uso
        bicicleta.setStatus(BicicletaStatus.EM_USO);

        // Lógica de log 

        // Lógica de Funcionario 

        // PERSISTIR AS ALTERAÇÕES
        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }
}