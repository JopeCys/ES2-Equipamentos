package scb.microsservico.equipamentos.mapper;

import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaCreateDTO;
import scb.microsservico.equipamentos.dto.Bicicleta.BicicletaResponseDTO;
import scb.microsservico.equipamentos.model.Bicicleta;

public class BicicletaMapper {
    // Converte entidade Bicicleta para DTO de resposta
    public static BicicletaResponseDTO toDTO(Bicicleta bicicleta) {
        BicicletaResponseDTO dto = new BicicletaResponseDTO();
        dto.setId(bicicleta.getId());
        dto.setNumero(bicicleta.getNumero());
        dto.setMarca(bicicleta.getMarca());
        dto.setModelo(bicicleta.getModelo());
        dto.setAno(bicicleta.getAno());
        dto.setLocalizacao(bicicleta.getLocalizacao());
        dto.setStatus(bicicleta.getStatus());
        return dto;
    }

    // Converte DTO de criação para entidade Bicicleta
    public static Bicicleta toEntity(BicicletaCreateDTO dto) {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca(dto.getMarca());
        bicicleta.setModelo(dto.getModelo());
        bicicleta.setAno(dto.getAno());
        return bicicleta;
    }
}