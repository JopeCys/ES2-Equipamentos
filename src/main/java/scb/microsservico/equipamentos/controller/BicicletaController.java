package scb.microsservico.equipamentos.controller;

import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaUpdateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.IntegrarBicicletaDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.RetirarBicicletaDTO;
import scb.microsservico.equipamentos.enums.BicicletaStatus;
import scb.microsservico.equipamentos.service.BicicletaService;

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
@RequestMapping("/bicicleta")
// Injeta dependências via construtor
@RequiredArgsConstructor
public class BicicletaController {
    // Serviço de bicicleta
    private final BicicletaService bicicletaService;

    @PostMapping
    public ResponseEntity<String> criarBicicleta(@Valid @RequestBody BicicletaCreateDTO dto) {
        bicicletaService.criarBicicleta(dto);
        return ResponseEntity.accepted().body("Bicicleta Cadastrada");
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaResponseDTO> buscarBicicletaPorId(@PathVariable Long idBicicleta) {
        BicicletaResponseDTO bicicleta = bicicletaService.buscarBicicletaPorId(idBicicleta);
        return ResponseEntity.ok(bicicleta);
    }

    @GetMapping
    public ResponseEntity<List<BicicletaResponseDTO>> buscarTodasBicicletas() {
        List<BicicletaResponseDTO> bicicletas = bicicletaService.buscarTodasBicicletas();
        return ResponseEntity.ok(bicicletas);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaResponseDTO> atualizarBicicleta(
            @PathVariable Long idBicicleta,
            @RequestBody BicicletaUpdateDTO dto) {
        BicicletaResponseDTO bicicletaAtualizada = bicicletaService.atualizarBicicleta(idBicicleta, dto);
        return ResponseEntity.ok(bicicletaAtualizada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<String> deletarBicicleta(@PathVariable Long idBicicleta) {
        bicicletaService.deletarBicicleta(idBicicleta);
        return ResponseEntity.accepted().body("Bicicleta Deletada");
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<BicicletaResponseDTO> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable String acao) {
        // Converte a string da URL para o Enum
        BicicletaStatus novoStatus = BicicletaStatus.valueOf(acao.toUpperCase());

        bicicletaService.alterarStatus(idBicicleta, novoStatus);
        BicicletaResponseDTO bicicletaAtualizada = bicicletaService.buscarBicicletaPorId(idBicicleta);
        return ResponseEntity.ok(bicicletaAtualizada);
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<String> integrarBicicletaNaRede(@Valid @RequestBody IntegrarBicicletaDTO integracaoRedeDTO) {
        bicicletaService.integrarBicicletaNaRede(integracaoRedeDTO);
        return ResponseEntity.accepted().body("Bicicleta Integrada");
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<String> retirarBicicletaDaRede(@Valid @RequestBody RetirarBicicletaDTO retirarBicicletaDTO) {
        bicicletaService.retirarBicicletaDaRede(retirarBicicletaDTO);
        return ResponseEntity.accepted().body("Bicicleta Retirada");
    }
}