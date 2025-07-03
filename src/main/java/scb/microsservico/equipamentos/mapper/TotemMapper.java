package scb.microsservico.equipamentos.mapper;

import scb.microsservico.equipamentos.dto.Totem.TotemCreateDTO;
import scb.microsservico.equipamentos.dto.Totem.TotemResponseDTO;
import scb.microsservico.equipamentos.model.Totem;

public class TotemMapper {
    // Converte entidade Totem para DTO de resposta
    public static TotemResponseDTO toDTO(Totem totem) {
        TotemResponseDTO dto = new TotemResponseDTO();
        dto.setId(totem.getId());
        dto.setLocalizacao(totem.getLocalizacao());
        dto.setDescricao(totem.getDescricao());
        return dto;
    }

    // Converte DTO de criação para entidade Totem
    public static Totem toEntity(TotemCreateDTO dto) {
        Totem totem = new Totem();
        totem.setLocalizacao(dto.getLocalizacao());
        totem.setDescricao(dto.getDescricao());
        // Trancas geralmente não são setadas na criação do Totem
        return totem;
    }
}