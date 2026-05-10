package com.example.demo.controller;

import com.example.demo.model.entidad.Carrito;
import com.example.demo.model.service.Interface.ICarritoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CarritoController {

    private final ICarritoService carritoService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Long idUsuario) {
        Carrito carrito = carritoService.obtenerCarritoActivo(idUsuario);
        return ResponseEntity.ok(new CarritoResponse(
                carrito.getIdCarrito(),
                idUsuario,
                carrito.getActivo() != null ? carrito.getActivo() : true
        ));
    }

    @GetMapping("/detalles/{idUsuario}")
    public ResponseEntity<List<DetalleResponse>> obtenerDetalles(@PathVariable Long idUsuario) {
        Carrito carrito = carritoService.obtenerCarritoActivo(idUsuario);
        List<DetalleResponse> detalles = new ArrayList<>();
        
        if (carrito.getDetalles() != null) {
            for (var d : carrito.getDetalles()) {
                String nombreProducto = d.getProducto() != null ? d.getProducto().getNombreProducto() : "Producto";
                detalles.add(new DetalleResponse(
                        d.getIdDetalleCarrito(),
                        d.getProducto().getIdProducto(),
                        nombreProducto,
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()
                ));
            }
        }
        return ResponseEntity.ok(detalles);
    }

    public record CarritoResponse(Long idCarrito, Long idUsuario, Boolean activo) {}

    public record DetalleResponse(
            Long idDetalle, 
            Long idProducto, 
            String nombreProducto, 
            Integer cantidad, 
            Double precioUnitario, 
            Double subtotal
    ) {}
}