package scb.microsservico.equipamentos.service;

import java.security.SecureRandom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import scb.microsservico.equipamentos.client.AluguelServiceClient;
import scb.microsservico.equipamentos.client.ExternoServiceClient;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.AcaoRetirar;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.enums.TrancaStatus;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaNotFoundException;
import scb.microsservico.equipamentos.exception.Bicicleta.BicicletaOcupadaException;
import scb.microsservico.equipamentos.exception.Tranca.TrancaNotFoundException;
import scb.microsservico.equipamentos.mapper.BicicletaMapper;
import scb.microsservico.equipamentos.model.Bicicleta;
import scb.microsservico.equipamentos.model.Tranca;
import scb.microsservico.equipamentos.repository.BicicletaRepository;
import scb.microsservico.equipamentos.repository.TrancaRepository;

@Service // Indica que é um serviço do Spring
@RequiredArgsConstructor // Injeta dependências via construtor
public class BicicletaService {
    
    // Repositórios para acesso ao banco
    private final BicicletaRepository bicicletaRepository; 
    private final TrancaRepository trancaRepository; 
    
    // Serviços auxiliares
    private final TrancaService trancaService;
    private final AluguelServiceClient aluguelServiceClient;
    private final ExternoServiceClient externoServiceClient;


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

    // Altera o status de uma bicicleta
    public void alterarStatus(Long idBicicleta, BicicletaStatus novoStatus) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);

        bicicleta.setStatus(novoStatus);

        bicicletaRepository.save(bicicleta);
    }

    // Integra uma bicicleta na rede, associando-a a uma tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void integrarBicicletaNaRede(IntegrarBicicletaDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(BicicletaNotFoundException::new);

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        // Pré-condições
        if (bicicleta.getStatus() != BicicletaStatus.NOVA && bicicleta.getStatus() != BicicletaStatus.EM_REPARO) {
            throw new IllegalStateException("A bicicleta deve estar com status 'NOVA' ou 'EM_REPARO'.");
        }
        if (tranca.getStatus() != TrancaStatus.LIVRE) {
            throw new IllegalStateException("A tranca deve estar com o status 'LIVRE'.");
        }

        // Simula o processo de registro de integração
        System.out.println("LOG: Registrando inclusão - Data/Hora: " + java.time.LocalDateTime.now() +
                           ", Número da Bicicleta: " + bicicleta.getNumero() +
                           ", Número da Tranca: " + tranca.getNumero());

        
        // Fecha tranca
        TrancarRequestDTO trancarRequestDTO = new TrancarRequestDTO();
        trancarRequestDTO.setIdBicicleta(bicicleta.getId());
        trancaService.trancarTranca(tranca.getId(), trancarRequestDTO);

        // Altera o status da bicicleta para "disponível"
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta);

        // Simulação de envio de email para o reparador
        try {
            String emailFuncionario = aluguelServiceClient.getEmailFuncionario(dto.getIdFuncionario());
            String assunto = "Bicicleta integrada ao sistema";
            String corpo = String.format("A bicicleta de número %d foi incluída na tranca de número %d.",
                                         bicicleta.getNumero(), tranca.getNumero());
            externoServiceClient.enviarEmail(emailFuncionario, assunto, corpo);
        } catch (Exception e) {
            System.err.println("ERRO: Não foi possível enviar o e-mail de notificação da integração: " + e.getMessage());
        }
    }

    @Transactional
    public void retirarBicicletaDaRede(RetirarBicicletaDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(BicicletaNotFoundException::new);

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        // Pré-condições
        if (bicicleta.getStatus() == BicicletaStatus.DISPONIVEL || tranca.getStatus() == TrancaStatus.LIVRE) {
            throw new IllegalStateException("A bicicleta já está disponível ou a tranca já está livre.");
        }

        // Simula o processo de solicitação de retirada
        System.out.println("LOG: Retirada solicitada para bicicleta: " + bicicleta.getNumero() + " na tranca: " + tranca.getNumero());

        // Abre tranca
        DestrancarRequestDTO destrancarRequestDTO = new DestrancarRequestDTO();
        destrancarRequestDTO.setIdBicicleta(bicicleta.getId());
        trancaService.destrancarTranca(dto.getIdTranca(), destrancarRequestDTO);

        // Altera o status da bicicleta de acordo com a ação solicitada
        if(dto.getAcao() == AcaoRetirar.REPARO) {
            bicicleta.setStatus(BicicletaStatus.EM_REPARO);
        } else if (dto.getAcao() == AcaoRetirar.APOSENTADORIA) {
            bicicleta.setStatus(BicicletaStatus.APOSENTADA);
        }
        bicicletaRepository.save(bicicleta);
        
        // Simula o processo de registro de integração
        System.out.println("LOG: Registro de Retirada - Data/Hora: " + java.time.LocalDateTime.now() +
                ", Reparador: " + dto.getIdFuncionario() +
                ", Número da Bicicleta: " + bicicleta.getNumero());

        // Simulação de envio de email para o reparador
        try {
            String emailReparador = aluguelServiceClient.getEmailFuncionario(dto.getIdFuncionario());
            String assunto = "Notificação de Retirada de Bicicleta";
            String corpo = String.format("A bicicleta de número %d foi retirada da tranca %d para %s.",
                    bicicleta.getNumero(), tranca.getNumero(), dto.getAcao().toString().toLowerCase());
            externoServiceClient.enviarEmail(emailReparador, assunto, corpo);
        } catch (Exception e) {
            System.err.println("ERRO: Não foi possível enviar o e-mail de notificação da retirada: " + e.getMessage());
        }
    }
}