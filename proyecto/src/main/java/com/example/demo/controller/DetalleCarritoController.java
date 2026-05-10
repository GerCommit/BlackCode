package com.example.demo.controller;

import com.example.demo.model.service.Interface.IDetalleCarritoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detalle-carrito")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DetalleCarritoController {

    private final IDetalleCarritoService detalleService;

    @PostMapping("/agregar")
    public ResponseEntity<AgregarResponse> agregarProducto(
            @RequestParam Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad
    ) {
        var detalle = detalleService.agregarProducto(idUsuario, idProducto, cantidad, null);
        return ResponseEntity.ok(new AgregarResponse(
                detalle.getIdDetalleCarrito(),
                cantidad,
                detalle.getSubtotal()
        ));
    }

    @PutMapping("/cantidad/{idDetalle}")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long idDetalle,
            @RequestParam Integer cantidad
    ) {
        var detalle = detalleService.actualizarCantidad(idDetalle, cantidad);
        return ResponseEntity.ok(new AgregarResponse(
                detalle.getIdDetalleCarrito(),
                detalle.getCantidad(),
                detalle.getSubtotal()
        ));
    }

    @DeleteMapping("/eliminar/{idDetalle}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long idDetalle) {
        detalleService.eliminarDetalle(idDetalle);
        return ResponseEntity.noContent().build();
    }

    public record AgregarResponse(Long idDetalleCarrito, Integer cantidad, Double subtotal) {}
}