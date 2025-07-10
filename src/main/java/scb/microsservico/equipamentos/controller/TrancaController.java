package scb.microsservico.equipamentos.controller;

import scb.microsservico.equipamentos.dto.Tranca.DestrancarRequestDTO;
import scb.microsservico.equipamentos.dto.Tranca.IntegrarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.RetirarTrancaDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaUpdateDTO;
import scb.microsservico.equipamentos.service.TrancaService;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancarRequestDTO;
import scb.microsservico.equipamentos.enums.TrancaStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;

import java.util.List;

// Define como controller REST
@RestController
// Define o endpoint base
@RequestMapping("/tranca")
// Injeta dependências via construtor
@RequiredArgsConstructor
public class TrancaController {
    // Serviço de tranca
    private final TrancaService trancaService;

    @PostMapping
    public ResponseEntity<String> criarTranca(@Valid @RequestBody TrancaCreateDTO dto) {
        trancaService.criarTranca(dto);
        return ResponseEntity.accepted().body("Dados Cadastrados");
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<TrancaResponseDTO> buscarTrancaPorId(@PathVariable Long idTranca) {
        TrancaResponseDTO tranca = trancaService.buscarTrancaPorId(idTranca);
        return ResponseEntity.ok(tranca);
    }

    @GetMapping
    public ResponseEntity<List<TrancaResponseDTO>> buscarTodasTrancas() {
        List<TrancaResponseDTO> trancas = trancaService.buscarTodasTrancas();
        return ResponseEntity.ok(trancas);
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<TrancaResponseDTO> atualizarTranca(
            @PathVariable Long idTranca,
            @Valid @RequestBody TrancaUpdateDTO dto) {
        TrancaResponseDTO trancaAtualizada = trancaService.atualizarTranca(idTranca, dto);
        return ResponseEntity.ok(trancaAtualizada);
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<String> deletarTranca(@PathVariable Long idTranca) {
        trancaService.deletarTranca(idTranca);
        return ResponseEntity.accepted().body("Tranca Deletada");
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<BicicletaResponseDTO> buscarBicicletaNaTranca(@PathVariable Long idTranca) {
        BicicletaResponseDTO bicicleta = trancaService.buscarBicicletaNaTranca(idTranca);
        return ResponseEntity.ok(bicicleta);
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<String> trancarTranca(
            @PathVariable Long idTranca,
            @RequestBody TrancarRequestDTO dto) {
        trancaService.trancarTranca(idTranca, dto);
        return ResponseEntity.accepted().body("Tranca trancada com sucesso");
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<String> destrancarTranca(
            @PathVariable Long idTranca,
            @RequestBody DestrancarRequestDTO dto) {
        trancaService.destrancarTranca(idTranca, dto);
        return ResponseEntity.accepted().body("Tranca destrancada com sucesso");
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<TrancaResponseDTO> alterarStatusTranca(@PathVariable Long idTranca, @PathVariable String acao) {
        // Converte a string da URL para o Enum
        TrancaStatus novoStatus = TrancaStatus.valueOf(acao.toUpperCase());

        trancaService.alterarStatus(idTranca, novoStatus);
        TrancaResponseDTO trancaAtualizada = trancaService.buscarTrancaPorId(idTranca);
        return ResponseEntity.ok(trancaAtualizada);
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarNaRede(@Valid @RequestBody IntegrarTrancaDTO dto) {
        trancaService.integrarNaRede(dto);
        return ResponseEntity.accepted().body("Tranca integrada com sucesso");
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarDaRede(@Valid @RequestBody RetirarTrancaDTO dto) {
        trancaService.retirarDaRede(dto);
        return ResponseEntity.accepted().body("Tranca retirada com sucesso");
    }
}