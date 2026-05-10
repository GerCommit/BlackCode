package com.example.demo.controller;

import com.example.demo.model.entidad.OrdenCompra;
import com.example.demo.model.service.Interface.IOrdenCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenCompraController {

    private final IOrdenCompraService service;

    @GetMapping("/")
    public ResponseEntity<List<OrdenCompra>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<OrdenCompra> guardar(@Valid @RequestBody OrdenCompra orden) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(orden));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<OrdenCompra> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<OrdenCompra> actualizar(@PathVariable Long id, @Valid @RequestBody OrdenCompra orden) {
        orden.setIdOrdenCompra(id); 
        return ResponseEntity.ok(service.guardar(orden));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
