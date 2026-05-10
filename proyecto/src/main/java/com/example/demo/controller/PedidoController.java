package com.example.demo.controller;

import com.example.demo.model.entidad.Pedido;
import com.example.demo.model.service.Interface.IPedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    private final IPedidoService service;

    @GetMapping("/metodos-pago")
    public ResponseEntity<List<String>> obtenerMetodosPago() {
        return ResponseEntity.ok(List.of("Efectivo", "Tarjeta", "Transferencia", "Yape", "Plin"));
    }

    @GetMapping("/")
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Pedido> checkout(@RequestBody CheckoutRequest request) {
        Pedido pedido = service.crearDesdeCarrito(
                request.idUsuario(),
                request.idInfoEnvio(),
                request.metodoPago()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PostMapping("/guardar")
    public ResponseEntity<Pedido> guardar(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(pedido));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @Valid @RequestBody Pedido pedido) {
        pedido.setIdPedido(id);
        return ResponseEntity.ok(service.guardar(pedido));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idPedido}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long idPedido, @RequestParam Long idUsuario) {
        return ResponseEntity.ok(service.cancelarPedido(idPedido, idUsuario));
    }

    @PutMapping("/{idPedido}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long idPedido, @RequestParam Pedido.Estado estado) {
        return ResponseEntity.ok(service.actualizarEstado(idPedido, estado));
    }

    public record CheckoutRequest(Long idUsuario, Long idInfoEnvio, Pedido.MetodoPago metodoPago) {
    }
}
