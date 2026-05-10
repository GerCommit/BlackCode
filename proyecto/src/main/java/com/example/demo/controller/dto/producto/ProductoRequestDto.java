package com.example.demo.controller.dto.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequestDto(
        @NotNull Long idCategoria,
        @NotBlank String codigoProducto,
        @NotBlank String nombreProducto,
        String descripcion,
        String material,
        String color,
        String tallaMedida,
        @NotNull @Min(0) Double precioVenta,
        @NotNull @Min(0) Double precioCompra,
        @NotNull @Min(0) Integer stockActual,
        @NotNull @Min(0) Integer stockMinimo,
        String marca,
        String imagenUrl,
        Boolean activo) {
}
