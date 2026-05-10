package com.example.demo.controller;

import com.example.demo.controller.dto.producto.ProductoRequestDto;
import com.example.demo.controller.dto.producto.ProductoResponseDto;
import com.example.demo.model.entidad.Producto;
import com.example.demo.model.mapper.ProductoMapper;
import com.example.demo.model.service.Interface.IProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService service;
    private final ProductoMapper mapper;

    @GetMapping({"", "/"})
    public ResponseEntity<List<ProductoResponseDto>> listar() {
        List<ProductoResponseDto> response = service.listar().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDto>> listarPorCategoria(@PathVariable String categoria) {
        List<ProductoResponseDto> response = service.listarPorCategoria(categoria).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guardar")
    public ResponseEntity<ProductoResponseDto> guardar(@Valid @RequestBody ProductoRequestDto request) {
        Producto guardado = service.guardar(mapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(guardado));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProductoResponseDto> buscar(@PathVariable Long id) {
        Producto producto = service.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(producto));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDto request) {
        Producto existente = service.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        mapper.applyToEntity(existente, request);
        existente.setIdProducto(id);
        Producto actualizado = service.guardar(existente);
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
