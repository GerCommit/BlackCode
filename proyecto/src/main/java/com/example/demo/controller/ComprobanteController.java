package com.example.demo.controller;

import com.example.demo.model.entidad.Comprobante;
import com.example.demo.model.service.Interface.IComprobanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ComprobanteController {

    private final IComprobanteService service;

    @GetMapping("/")
    public ResponseEntity<List<Comprobante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<Comprobante> guardar(@Valid @RequestBody Comprobante comprobante) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(comprobante));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Comprobante> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Comprobante> actualizar(@PathVariable Long id, @Valid @RequestBody Comprobante comprobante) {
        comprobante.setIdComprobante(id);
        return ResponseEntity.ok(service.guardar(comprobante));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
