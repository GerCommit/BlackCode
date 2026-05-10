package com.example.demo.model.mapper;

import com.example.demo.controller.dto.producto.ProductoRequestDto;
import com.example.demo.controller.dto.producto.ProductoResponseDto;
import com.example.demo.model.entidad.Categoria;
import com.example.demo.model.entidad.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoResponseDto toResponse(Producto producto) {
        if (producto == null) {
            return null;
        }
        return new ProductoResponseDto(
                producto.getIdProducto(),
                producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : null,
                producto.getCodigoProducto(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getMaterial(),
                producto.getColor(),
                producto.getTallaMedida(),
                producto.getPrecioVenta(),
                producto.getPrecioCompra(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.getMarca(),
                producto.getImagenUrl(),
                producto.getActivo());
    }

    public Producto toEntity(ProductoRequestDto request) {
        Producto producto = new Producto();
        applyToEntity(producto, request);
        return producto;
    }

    public void applyToEntity(Producto producto, ProductoRequestDto request) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(request.idCategoria());

        producto.setCategoria(categoria);
        producto.setCodigoProducto(request.codigoProducto());
        producto.setNombreProducto(request.nombreProducto());
        producto.setDescripcion(request.descripcion());
        producto.setMaterial(request.material());
        producto.setColor(request.color());
        producto.setTallaMedida(request.tallaMedida());
        producto.setPrecioVenta(request.precioVenta());
        producto.setPrecioCompra(request.precioCompra());
        producto.setStockActual(request.stockActual());
        producto.setStockMinimo(request.stockMinimo());
        producto.setMarca(request.marca());
        producto.setImagenUrl(request.imagenUrl());
        producto.setActivo(request.activo() != null ? request.activo() : true);
    }
}
