package scb.microsservico.equipamentos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import scb.microsservico.equipamentos.controller.AdminController;
import scb.microsservico.equipamentos.service.AdminService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminService adminService;
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        adminService = Mockito.mock(AdminService.class);
        adminController = new AdminController(adminService);
    }

    @Test
    public void testRestaurarBanco_callsServiceAndReturnsOk() {
        ResponseEntity<String> response = adminController.restaurarBanco();

        verify(adminService, times(1)).restaurarBanco();
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Banco de dados de equipamentos restaurado com sucesso.", response.getBody());
    }
}
