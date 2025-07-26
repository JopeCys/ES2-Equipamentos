package scb.microsservico.equipamentos.Tranca.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import scb.microsservico.equipamentos.dto.Tranca.TrancaCreateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrancaCreateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(10);
        dto.setAnoDeFabricacao("2023");
        dto.setModelo("Modelo X");

        assertEquals(10, dto.getNumero());
        assertEquals("2023", dto.getAnoDeFabricacao());
        assertEquals("Modelo X", dto.getModelo());
    }

    @Test
    void testEqualsAndHashCode() {
        TrancaCreateDTO dto1 = new TrancaCreateDTO();
        dto1.setNumero(10);
        dto1.setAnoDeFabricacao("2023");
        dto1.setModelo("Modelo X");

        TrancaCreateDTO dto2 = new TrancaCreateDTO();
        dto2.setNumero(10);
        dto2.setAnoDeFabricacao("2023");
        dto2.setModelo("Modelo X");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(5);
        dto.setAnoDeFabricacao("2022");
        dto.setModelo("M2");

        String str = dto.toString();
        assertTrue(str.contains("numero=5"));
        assertTrue(str.contains("anoDeFabricacao=2022"));
        assertTrue(str.contains("modelo=M2"));
    }

    @Test
    void testAnoDeFabricacaoNaoVazio() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(123);
        dto.setAnoDeFabricacao("");
        dto.setModelo("Modelo Válido");

        Set<ConstraintViolation<TrancaCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testModeloNaoVazio() {
        TrancaCreateDTO dto = new TrancaCreateDTO();
        dto.setNumero(123);
        dto.setAnoDeFabricacao("2023");
        dto.setModelo("");

        Set<ConstraintViolation<TrancaCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}