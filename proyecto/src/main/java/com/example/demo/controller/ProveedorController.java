package com.example.demo.controller;

import com.example.demo.model.entidad.Proveedor;
import com.example.demo.model.service.Interface.IProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProveedorController {

    private final IProveedorService service;

    @GetMapping("/")
    public ResponseEntity<List<Proveedor>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<Proveedor> guardar(@Valid @RequestBody Proveedor proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(proveedor));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Proveedor> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
