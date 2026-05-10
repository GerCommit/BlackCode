package com.example.demo.controller;

import com.example.demo.model.entidad.DetalleOrdenCompra;
import com.example.demo.model.service.Interface.IDetalleOrdenCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-orden")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DetalleOrdenCompraController {

    private final IDetalleOrdenCompraService service;

    @GetMapping("/")
    public ResponseEntity<List<DetalleOrdenCompra>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<DetalleOrdenCompra> guardar(@Valid @RequestBody DetalleOrdenCompra detalle) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(detalle));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<DetalleOrdenCompra> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DetalleOrdenCompra> actualizar(@PathVariable Long id,  @Valid @RequestBody DetalleOrdenCompra detalle) {
        detalle.setIdDetalleOrden(id); // ← CORREGIDO
        return ResponseEntity.ok(service.guardar(detalle));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
