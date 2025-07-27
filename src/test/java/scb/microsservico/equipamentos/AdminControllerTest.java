package scb.microsservico.equipamentos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import scb.microsservico.equipamentos.controller.AdminController;
import scb.microsservico.equipamentos.service.AdminService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    public void testRestaurarBanco() throws Exception {
        mockMvc.perform(get("/restaurarBanco"))
                .andExpect(status().isOk())
                .andExpect(content().string("Banco de dados de equipamentos restaurado com sucesso."));

        verify(adminService, times(1)).restaurarBanco();
    }

    @Test
    public void testRestaurarDados() throws Exception {
        mockMvc.perform(get("/restaurarDados"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dados de equipamentos restaurados com sucesso."));

        verify(adminService, times(1)).restaurarDados();
    }
}
