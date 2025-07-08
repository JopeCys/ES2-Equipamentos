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

@RestController // Define como controller REST
@RequestMapping("/bicicleta") // Define o endpoint base
@RequiredArgsConstructor // Injeta dependências via construtor
public class BicicletaController 
{
    private final BicicletaService bicicletaService; // Serviço de bicicleta

    @PostMapping
    public ResponseEntity<String> criarBicicleta(@RequestBody BicicletaCreateDTO dto) {
        bicicletaService.criarBicicleta(dto); // Cria nova bicicleta
        return ResponseEntity.accepted().body("Bicicleta Cadastrada");
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaResponseDTO> buscarBicicletaPorId(@PathVariable Long idBicicleta) {
        BicicletaResponseDTO bicicleta = bicicletaService.buscarBicicletaPorId(idBicicleta); // Busca por ID
        return ResponseEntity.ok(bicicleta);
    }

    @GetMapping
    public ResponseEntity<List<BicicletaResponseDTO>> buscarTodasBicicletas() {
        List<BicicletaResponseDTO> bicicletas = bicicletaService.buscarTodasBicicletas(); // Busca todas as bicicletas
        return ResponseEntity.ok(bicicletas);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaResponseDTO> atualizarBicicleta(
            @PathVariable Long idBicicleta,
            @RequestBody BicicletaUpdateDTO dto) {
        BicicletaResponseDTO bicicletaAtualizada = bicicletaService.atualizarBicicleta(idBicicleta, dto); // Atualiza bicicleta
        return ResponseEntity.ok(bicicletaAtualizada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<String> deletarBicicleta(@PathVariable Long idBicicleta) {
        bicicletaService.deletarBicicleta(idBicicleta); // Deleta bicicleta
        return ResponseEntity.accepted().body("Bicicleta Deletada");
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<BicicletaResponseDTO> alterarStatusBicicleta(@PathVariable Long idBicicleta, @PathVariable BicicletaStatus acao) {
        bicicletaService.alterarStatus(idBicicleta, acao);
        BicicletaResponseDTO bicicleta = bicicletaService.buscarBicicletaPorId(idBicicleta);
        return ResponseEntity.ok(bicicleta);
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