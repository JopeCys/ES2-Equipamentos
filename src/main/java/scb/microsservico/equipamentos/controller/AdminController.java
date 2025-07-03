package scb.microsservico.equipamentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scb.microsservico.equipamentos.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/restaurarBanco")
    public ResponseEntity<String> restaurarBanco() {
        adminService.restaurarBanco();
        return ResponseEntity.ok("Banco de dados de equipamentos restaurado com sucesso.");
    }
}