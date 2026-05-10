package com.example.demo.controller.dto.producto;

public record ProductoResponseDto(
        Long idProducto,
        Long idCategoria,
        String codigoProducto,
        String nombreProducto,
        String descripcion,
        String material,
        String color,
        String tallaMedida,
        Double precioVenta,
        Double precioCompra,
        Integer stockActual,
        Integer stockMinimo,
        String marca,
        String imagenUrl,
        Boolean activo) {
}
