package com.example.demo.controller;

import com.example.demo.model.entidad.DetallePedido;
import com.example.demo.model.service.Interface.IDetallePedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DetallePedidoController {

    private final IDetallePedidoService service;

    @GetMapping("/")
    public ResponseEntity<List<DetallePedido>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/guardar")
    public ResponseEntity<DetallePedido> guardar(@Valid @RequestBody DetallePedido detalle) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(detalle));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<DetallePedido> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DetallePedido> actualizar(@PathVariable Long id, @Valid @RequestBody DetallePedido detalle) {
        detalle.setIdDetallePedido(id);
        return ResponseEntity.ok(service.guardar(detalle));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
