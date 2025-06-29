package scb.microsservico.equipamentos.controller;

import scb.microsservico.equipamentos.dto.TrancaResponseDTO;
import scb.microsservico.equipamentos.dto.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.TrancaUpdateDTO;
import scb.microsservico.equipamentos.service.TrancaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController // Define como controller REST
@RequestMapping("/tranca") // Define o endpoint base
@RequiredArgsConstructor // Injeta dependências via construtor
public class TrancaController 
{
    private final TrancaService trancaService; // Serviço de tranca
    
    @PostMapping
    public ResponseEntity<String> criarTranca(@RequestBody TrancaCreateDTO dto) {
        trancaService.criarTranca(dto); // Cria nova tranca
        return ResponseEntity.accepted().body("Código 202: Dados Cadastrados");
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<TrancaResponseDTO> buscarTrancaPorId(@PathVariable Long idTranca) {
        TrancaResponseDTO tranca = trancaService.buscarTrancaPorId(idTranca); // Busca por ID
        return ResponseEntity.ok(tranca);
    }

    @GetMapping
    public ResponseEntity<List<TrancaResponseDTO>> buscarTodasTrancas() {
        List<TrancaResponseDTO> trancas = trancaService.buscarTodasTrancas(); // Busca todas as trancas
        return ResponseEntity.ok(trancas);
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<TrancaResponseDTO> atualizarTranca(
            @PathVariable Long idTranca,
            @RequestBody TrancaUpdateDTO dto) {
        TrancaResponseDTO trancaAtualizada = trancaService.atualizarTranca(idTranca, dto); // Atualiza tranca
        return ResponseEntity.ok(trancaAtualizada);
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<String> deletarTranca(@PathVariable Long idTranca) {
        trancaService.deletarTranca(idTranca); // Deleta tranca
        return ResponseEntity.accepted().body("Código 202: Tranca Deletada");
    }

}