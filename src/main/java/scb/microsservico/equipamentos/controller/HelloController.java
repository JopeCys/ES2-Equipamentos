package scb.microsservico.equipamentos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Define como controller REST
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String helloWorld() { return "Hello World"; }
}
