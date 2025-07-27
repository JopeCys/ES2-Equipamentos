package scb.microsservico.equipamentos.service;

import java.security.SecureRandom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

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
    private final OperacaoService operacaoService;
    private final EmailService emailService;


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
    public void alterarStatus(Long idBicicleta, BicicletaStatus acao) {
        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(BicicletaNotFoundException::new);

        bicicleta.setStatus(acao);

        bicicletaRepository.save(bicicleta);
    }

    // Integra uma bicicleta na rede, associando-a a uma tranca
    @Transactional // Garante que todas as operações sejam atômicas
    public void integrarBicicletaNaRede(IntegrarBicicletaDTO dto) {
        // Busca bicicleta e tranca pelo ID
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(BicicletaNotFoundException::new);

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        // Valida pré-condições de status
        if (bicicleta.getStatus() != BicicletaStatus.NOVA && bicicleta.getStatus() != BicicletaStatus.EM_REPARO) {
            throw new IllegalStateException("A bicicleta deve estar com status 'NOVA' ou 'EM_REPARO'.");
        }
        if (tranca.getStatus() != TrancaStatus.LIVRE) {
            throw new IllegalStateException("A tranca deve estar com o status 'LIVRE'.");
        }
        
        // Associa bicicleta à tranca e atualiza status
        TrancarRequestDTO trancarRequestDTO = new TrancarRequestDTO();
        trancarRequestDTO.setBicicleta(bicicleta.getId());
        trancaService.trancarTranca(tranca.getId(), trancarRequestDTO);

        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicletaRepository.save(bicicleta);
        
        // Registra operação e envia notificação
        String descricao = String.format(
            "Bicicleta %d integrada na tranca %d pelo funcionário %d.",
            bicicleta.getNumero(), tranca.getNumero(), dto.getIdFuncionario()
        );
        operacaoService.registrarOperacao("INTEGRACAO", descricao, dto.getIdFuncionario());

        String assunto = "Bicicleta integrada ao sistema";
        String mensagem = String.format("A bicicleta de número %d foi incluída na tranca de número %d.",
            bicicleta.getNumero(), tranca.getNumero());
        emailService.enviarEmailNotificacao(dto.getIdFuncionario(), assunto, mensagem);
    }
    
    // Retira uma bicicleta da rede, desassociando-a de uma tranca
    @Transactional
    public void retirarBicicletaDaRede(RetirarBicicletaDTO dto) {
        // Busca bicicleta e tranca pelo ID
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(BicicletaNotFoundException::new);

        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(TrancaNotFoundException::new);

        // Valida pré-condições de status
        if (bicicleta.getStatus() == BicicletaStatus.DISPONIVEL || tranca.getStatus() == TrancaStatus.LIVRE) {
            throw new IllegalStateException("A bicicleta já está disponível ou a tranca já está livre.");
        }

        // Desassocia bicicleta da tranca e atualiza status
        DestrancarRequestDTO destrancarRequestDTO = new DestrancarRequestDTO();
        destrancarRequestDTO.setBicicleta(bicicleta.getId());
        trancaService.destrancarTranca(dto.getIdTranca(), destrancarRequestDTO);

        if(dto.getStatusAcaoReparador() == AcaoRetirar.EM_REPARO) {
            bicicleta.setStatus(BicicletaStatus.EM_REPARO);
        } else if (dto.getStatusAcaoReparador() == AcaoRetirar.APOSENTADA) {
            bicicleta.setStatus(BicicletaStatus.APOSENTADA);
        }
        bicicletaRepository.save(bicicleta);
        
        // Registra operação e envia notificação
        String descricao = String.format(
            "Bicicleta %d retirada da tranca %d pelo funcionário %d.",
            bicicleta.getNumero(), tranca.getNumero(), dto.getIdFuncionario()
        );
        operacaoService.registrarOperacao("RETIRADA", descricao, dto.getIdFuncionario());

        String assunto = "Bicicleta retirada do sistema";
        String mensagem = String.format("A bicicleta de número %d foi retirada da tranca de número %d.",
            bicicleta.getNumero(), tranca.getNumero());
        emailService.enviarEmailNotificacao(dto.getIdFuncionario(), assunto, mensagem);
    }
}