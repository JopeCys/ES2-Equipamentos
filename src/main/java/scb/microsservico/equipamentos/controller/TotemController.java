package scb.microsservico.equipamentos.controller;

import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemUpdateDTO;
import scb.microsservico.equipamentos.service.TotemService;

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
@RequestMapping("/totem") // Define o endpoint base
@RequiredArgsConstructor // Injeta dependências via construtor
public class TotemController 
{
    private final TotemService totemService; // Serviço de totem

    @PostMapping
    public ResponseEntity<String> criarTotem(@RequestBody TotemCreateDTO dto) {
        totemService.criarTotem(dto); // Cria novo totem
        return ResponseEntity.accepted().body("Código 202: Totem Cadastrado");
    }

    @GetMapping("/{idTotem}")
    public ResponseEntity<TotemResponseDTO> buscarTotemPorId(@PathVariable Long idTotem) {
        TotemResponseDTO totem = totemService.buscarTotemPorId(idTotem); // Busca por ID
        return ResponseEntity.ok(totem);
    }

    @GetMapping
    public ResponseEntity<List<TotemResponseDTO>> buscarTodosTotens() {
        List<TotemResponseDTO> totens = totemService.buscarTodosTotens(); // Busca todos os totens
        return ResponseEntity.ok(totens);
    }

    @PutMapping("/{idTotem}")
    public ResponseEntity<TotemResponseDTO> atualizarTotem(
            @PathVariable Long idTotem,
            @RequestBody TotemUpdateDTO dto) {
        TotemResponseDTO totemAtualizado = totemService.atualizarTotem(idTotem, dto); // Atualiza totem
        return ResponseEntity.ok(totemAtualizado);
    }

    @DeleteMapping("/{idTotem}")
    public ResponseEntity<String> deletarTotem(@PathVariable Long idTotem) {
        totemService.deletarTotem(idTotem); // Deleta totem
        return ResponseEntity.accepted().body("Código 202: Totem Deletado");
    }
}