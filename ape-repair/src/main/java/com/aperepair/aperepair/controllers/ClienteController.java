package com.aperepair.aperepair.controllers;

import com.aperepair.aperepair.models.Cliente;
import com.aperepair.aperepair.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes.isEmpty()) {
            System.out.println("INFO - Não há clientes cadastrados");
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(clientes);
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente novoCliente) {
        clienteRepository.save(novoCliente);

        System.out.println(String.format("INFO - Cliente: %s foi inserido no banco de dados", novoCliente));

        return ResponseEntity.status(201).body(novoCliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
//       Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        return ResponseEntity.of(clienteRepository.findById(id));
//        if (optionalCliente.isPresent()) {
//            return ResponseEntity.status(200).body(optionalCliente.get());
//        }
//
//        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Integer id,
            @RequestBody Cliente clienteAtualizado
    ) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.save(clienteAtualizado);
            System.out.println(String.format("INFO - cliente de id: %d foi atualizado com sucesso", id));
            return ResponseEntity.status(200).body(clienteAtualizado);
        }

        System.out.println(String.format("ERRO - cliente de id: %d não foi encontrado", id));
        return ResponseEntity.status(404).build();
    }
}
