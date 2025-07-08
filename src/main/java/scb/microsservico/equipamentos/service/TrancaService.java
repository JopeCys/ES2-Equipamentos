package scb.microsservico.equipamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.IntegrarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.RetirarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaOcupadaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaLivreException;
import scb.microsservico.equipamentos.mapper.TrancaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Totem;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.TrancaRepository;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TotemRepository;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class TrancaService {
    private final TrancaRepository trancaRepository; // Repositório para acesso ao banco
    private final BicicletaRepository bicicletaRepository; // Repositório para acesso ao totem

    private final TotemRepository totemRepository; // Repositório para acesso ao totem

    // Cria uma nova tranca a partir do DTO
    public void criarTranca(TrancaCreateDTO dto) {
        Tranca tranca = TrancaMapper.toEntity(dto);
        tranca.setStatus(TrancaStatus.NOVA); // Define o status padrão aqui
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

    // Busca a bicicleta na tranca, se a tranca estiver OCUPADA
    public BicicletaResponseDTO buscarBicicletaNaTranca(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        if (!TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            throw new BicicletaNotFoundException(); // Ou crie exceção específica se preferir
        }

        if (tranca.getBicicleta() == null) {
            throw new BicicletaNotFoundException();
        }

        return bicicletaRepository.findByNumero(tranca.getBicicleta())
                .map(BicicletaMapper::toDTO)
                .orElseThrow(BicicletaNotFoundException::new);
    }

    public void trancarTranca(Long idTranca, TrancarRequestDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        if (TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            throw new TrancaOcupadaException();
        }

        if (dto != null && dto.getIdBicicleta() != null) {
            // Busca bicicleta pelo número
            var bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                    .orElseThrow(BicicletaNotFoundException::new);

            // Atualiza status da bicicleta
            bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
            bicicletaRepository.save(bicicleta);

            // Associa bicicleta à tranca
            tranca.setBicicleta(bicicleta.getNumero());
        }

        tranca.setStatus(TrancaStatus.OCUPADA);
        trancaRepository.save(tranca);
    }

    public void destrancarTranca(Long idTranca, DestrancarRequestDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        if (!TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            // Supondo que você tenha uma exceção para este caso
            throw new TrancaLivreException("A tranca já está livre e não pode ser destrancada.");
        }

        if (dto != null && dto.getIdBicicleta() != null) {
            // Busca a bicicleta pelo ID fornecido
            Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                    .orElseThrow(BicicletaNotFoundException::new);

            // Atualiza o status da bicicleta para EM_USO
            bicicleta.setStatus(BicicletaStatus.EM_USO);
            bicicletaRepository.save(bicicleta);

            // Desassocia a bicicleta da tranca
            tranca.setBicicleta(null);
        }

        // 4. Altera o status da tranca para LIVRE e salva as alterações
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);
    }

    public void alterarStatus(Long idTranca, TrancaStatus novoStatus) {
        // Busca a tranca no banco de dados ou lança uma exceção se não encontrar.
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        // Define o novo status na entidade.
        tranca.setStatus(novoStatus);

        // Salva a entidade Tranca com o status atualizado.
        trancaRepository.save(tranca);
    }

    public void integrarNaRede(IntegrarTrancaDTO dto) {
        // Busca a tranca pelo ID
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        // Busca o totem pelo ID
        Totem totem = totemRepository.findById(dto.getIdTotem())
                .orElseThrow(TotemNotFoundException::new);

        // Valida se a tranca está em um status que permite a integração (NOVA ou EM_REPARO)
        if (tranca.getStatus() != TrancaStatus.NOVA && tranca.getStatus() != TrancaStatus.EM_REPARO) {
            throw new IllegalStateException("A tranca com ID " + tranca.getId() + " não pode ser integrada pois seu status é " + tranca.getStatus());
        }

        // Adiciona a tranca à lista de trancas do totem
        totem.getTrancas().add(tranca);

        // Altera o status da tranca para LIVRE
        tranca.setStatus(TrancaStatus.LIVRE);

         // Lógica de log 

        // Lógica de funcionario
        // ...

        // Salva as alterações em ambas as entidades
        // O @Transactional garante que ambas as operações ocorram com sucesso ou nenhuma delas.
        totemRepository.save(totem);
        trancaRepository.save(tranca);
    }

     public void retirarDaRede(RetirarTrancaDTO dto) { // <-- NOME DO MÉTODO ALTERADO AQUI
        // Busca a tranca e o totem
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
            .orElseThrow(TrancaNotFoundException::new);

        // Busca o totem pelo ID
        Totem totem = totemRepository.findById(dto.getIdTotem())
                .orElseThrow(TotemNotFoundException::new);

        //Remove a tranca da lista de trancas do totem
        boolean foiRemovida = totem.getTrancas().remove(tranca);
        if(!foiRemovida){
             throw new IllegalStateException("A tranca " + tranca.getId() + " não foi encontrada no totem " + totem.getId());
        }

        // Altera o status da tranca para APOSENTADA
        tranca.setStatus(TrancaStatus.APOSENTADA);
        
        // Lógica de log
        // ...

        // Lógica de Funcionario
        // ...

        // Salva as alterações
        totemRepository.save(totem);
        trancaRepository.save(tranca);
    }
}