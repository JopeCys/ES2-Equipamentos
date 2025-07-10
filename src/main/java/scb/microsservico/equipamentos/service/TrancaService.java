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
import scb.microsservico.equipamentos.enums.AcaoRetirar;
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
import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Totem.TotemNotFoundException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaJaIntegradaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNaoIntegradaException; 


import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class TrancaService {

    // Repositórios para acesso ao banco
    private final TrancaRepository trancaRepository; 
    private final BicicletaRepository bicicletaRepository; 
    private final TotemRepository totemRepository;
    
    // Serviços auxiliares
    private final ExternoServiceClient externoServiceClient; 
    private final AluguelServiceClient aluguelServiceClient; 

    
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

    // Faz o processo de tranca, de associação e troca de status entre Bicicleta e Tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void trancarTranca(Long idTranca, TrancarRequestDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        if (TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            throw new TrancaOcupadaException();
        }

        // Para haver alteração, idBicicleta deve
        if (dto != null && dto.getIdBicicleta() != null) {
            Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                    .orElseThrow(BicicletaNotFoundException::new);

            bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
            bicicleta.setLocalizacao(tranca.getLocalizacao()); // Define a localização da bicicleta
            bicicletaRepository.save(bicicleta);
            
            tranca.setBicicleta(bicicleta.getNumero()); // Associa a bicicleta da tranca
        }

        tranca.setStatus(TrancaStatus.OCUPADA);
        trancaRepository.save(tranca);
    }

    // Faz o processo de destranca, de desassociação e troca de status entre Bicicleta e Tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void destrancarTranca(Long idTranca, DestrancarRequestDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        if (!TrancaStatus.OCUPADA.equals(tranca.getStatus())) {
            throw new TrancaLivreException();
        }

        if (dto != null && dto.getIdBicicleta() != null) {
            Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                    .orElseThrow(BicicletaNotFoundException::new);

            bicicleta.setStatus(BicicletaStatus.EM_USO);
            bicicleta.setLocalizacao(null); // Limpa a localização da bicicleta
            bicicletaRepository.save(bicicleta);
            
            tranca.setBicicleta(null); // Desassocia a bicicleta da tranca
        }

        // Altera o status da tranca para LIVRE
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);
    }

    // Altera o status de uma tranca
    public void alterarStatus(Long idTranca, TrancaStatus novoStatus) {
        Tranca tranca = trancaRepository.findById(idTranca)
                .orElseThrow(TrancaNotFoundException::new);

        tranca.setStatus(novoStatus);

        trancaRepository.save(tranca);
    }

    // Integra uma bicicleta na rede, associando-a a uma tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void integrarNaRede(IntegrarTrancaDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        Totem totem = totemRepository.findById(dto.getIdTotem())
                .orElseThrow(TotemNotFoundException::new);

        // Pré-condições
        if (tranca.getStatus() != TrancaStatus.NOVA && tranca.getStatus() != TrancaStatus.EM_REPARO) {
            throw new TrancaJaIntegradaException();
        }

        // Adiciona a tranca à lista de trancas do totem
        // Verifica se a lista de trancas do totem é nula e inicializa se for o caso
        if (totem.getTrancas() == null) {
            totem.setTrancas(new java.util.ArrayList<>());
        }
        totem.getTrancas().add(tranca);

        // Altera o status da tranca para LIVRE
        tranca.setStatus(TrancaStatus.LIVRE);
        // Define a localização da tranca pela localização do totem
        tranca.setLocalizacao(totem.getLocalizacao());

        // Simula o processo de registro de integração
        System.out.println("LOG: Tranca " + tranca.getId() + " integrada ao totem " + totem.getId() + " em " + java.time.LocalDateTime.now() +
                           "LOG: Matrícula do reparador: " + dto.getIdFuncionario() +
                           "LOG: Número da tranca: " + tranca.getNumero());


        // Simulação de envio de email para o reparador
        try {
            String emailIntegracao = aluguelServiceClient.getEmailFuncionario(dto.getIdFuncionario());
            String assunto = "Integração de Tranca Realizada";
            String corpo = String.format("A tranca %d (Número: %d) foi integrada com sucesso ao totem %d na localização '%s' pelo funcionário %d.",
                tranca.getId(), tranca.getNumero(), totem.getId(), totem.getLocalizacao(), dto.getIdFuncionario());
            externoServiceClient.enviarEmail(emailIntegracao, assunto, corpo);
        } catch (Exception e) {
            System.err.println("ERRO: Não foi possível enviar o e-mail de integração: " + e.getMessage());
        }

        // Salva as alterações em ambas as entidades
        totemRepository.save(totem);
        trancaRepository.save(tranca);
    }

    // Retira uma bicicleta da rede, deassociando-a a uma tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void retirarDaRede(RetirarTrancaDTO dto) {
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
            .orElseThrow(TrancaNotFoundException::new);

        Totem totem = totemRepository.findById(dto.getIdTotem())
                .orElseThrow(TotemNotFoundException::new);

        // Pré-condições
        boolean foiRemovida = totem.getTrancas().removeIf(t -> t.getId().equals(tranca.getId()));
        if(!foiRemovida){
             throw new TrancaNaoIntegradaException(); 
        } else {
            // Limpa localização da tranca
            tranca.setLocalizacao(null);
        }

        // Define o status da tranca com base na ação
        if (dto.getAcao() == AcaoRetirar.REPARO) {
            tranca.setStatus(TrancaStatus.EM_REPARO);
        } else if (dto.getAcao() == AcaoRetirar.APOSENTADORIA) {
            tranca.setStatus(TrancaStatus.APOSENTADA);
        }

        // Simula o processo de registro de retirada
        System.out.printf("LOG: Retirada da tranca %d. Ação: %s. Data/Hora: %s. Reparador: %d.%n",
            tranca.getNumero(),
            dto.getAcao().toString(),
            java.time.LocalDateTime.now(),
            dto.getIdFuncionario());

        // Simulação de envio de email para o reparador
        try {
            String emailReparador = aluguelServiceClient.getEmailFuncionario(dto.getIdFuncionario());
            String assunto = "Notificação de Retirada de Tranca do Totem";
            String corpo = String.format("A tranca de número %d foi retirada do totem %d (Local: %s) para %s pelo funcionário de ID %d.",
                tranca.getNumero(), totem.getId(), totem.getLocalizacao(), dto.getAcao().toString().toLowerCase(), dto.getIdFuncionario());
            
            externoServiceClient.enviarEmail(emailReparador, assunto, corpo);
        } catch (Exception e) {
            System.err.println("ERRO: Não foi possível enviar o e-mail de notificação da retirada: " + e.getMessage());
        }
        
        // Salva as alterações no banco de dados
        totemRepository.save(totem);
        trancaRepository.save(tranca);
    }
}