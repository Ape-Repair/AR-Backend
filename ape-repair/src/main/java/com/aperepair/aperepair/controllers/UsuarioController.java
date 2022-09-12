package com.aperepair.aperepair.controllers;

import com.aperepair.aperepair.models.Servico;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/valor-servico")
    public Double getValorServico(@RequestBody Servico servico) {
        Double total = 0.0;
        total = (servico.getCustoServico() * servico.getDuracaoServicoDias()) * 1.1;
        System.out.println(String.format("Valor total a ser pago pelo cliente: %.1f", total));
        return total;
    }

}
