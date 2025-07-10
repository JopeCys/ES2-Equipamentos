package scb.microsservico.equipamentos.mapper;

import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;
import scb.microsservico.equipamentos.dto.Tranca.TrancaResponseDTO;
import scb.microsservico.equipamentos.model.Tranca;

public class TrancaMapper {
    // Converte entidade Tranca para DTO de resposta
    public static TrancaResponseDTO toDTO(Tranca tranca) {
        TrancaResponseDTO dto = new TrancaResponseDTO();
        dto.setId(tranca.getId());
        dto.setBicicleta(tranca.getBicicleta());
        dto.setNumero(tranca.getNumero());
        dto.setLocalizacao(tranca.getLocalizacao());
        dto.setAnoDeFabricacao(tranca.getAnoDeFabricacao());
        dto.setModelo(tranca.getModelo());
        dto.setStatus(tranca.getStatus());
        return dto;
    }

    // Converte DTO de criação para entidade Tranca
    public static Tranca toEntity(TrancaCreateDTO dto) {
        Tranca tranca = new Tranca();
        tranca.setNumero(dto.getNumero());
        tranca.setAnoDeFabricacao(dto.getAnoDeFabricacao());
        tranca.setModelo(dto.getModelo());
        return tranca;
    }
}