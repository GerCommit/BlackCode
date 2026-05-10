package com.example.demo.controller;

import com.example.demo.model.entidad.Rol;
import com.example.demo.model.service.Interface.IRolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RolController {

    private final IRolService service;

    @GetMapping("/")
    public ResponseEntity<List<Rol>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<Rol> guardar(@Valid @RequestBody Rol rol) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(rol));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Rol> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @Valid @RequestBody Rol rol) {
        rol.setIdRol(id);
        return ResponseEntity.ok(service.guardar(rol));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
