package com.example.demo.model.service.Class;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.*;
import com.example.demo.model.repository.*;
import com.example.demo.model.service.Interface.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleCarritoService implements IDetalleCarritoService {

    private final IDetalleCarritoRepository detalleRepo;
    private final ICarritoService carritoService;
    private final IProductoRepository productoRepo;

    @Override
    @Transactional
    public DetalleCarrito agregarProducto(Long idUsuario, Long idProducto, Integer cantidad, Double precio) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = carritoService.obtenerCarritoActivo(idUsuario);
        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        if (producto.getStockActual() == null || producto.getStockActual() <= 0) {
            throw new IllegalArgumentException("Producto sin stock disponible");
        }

        double precioUnitario = producto.getPrecioVenta() != null ? producto.getPrecioVenta() : precio;
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("Precio de producto inválido");
        }

        DetalleCarrito detalle = detalleRepo.findByCarritoIdCarritoAndProductoIdProducto(
                carrito.getIdCarrito(), idProducto).orElseGet(DetalleCarrito::new);

        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        int nuevaCantidad = (detalle.getCantidad() == null ? 0 : detalle.getCantidad()) + cantidad;
        if (nuevaCantidad > producto.getStockActual()) {
            throw new IllegalArgumentException("La cantidad solicitada excede el stock disponible");
        }

        detalle.setCantidad(nuevaCantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(precioUnitario * nuevaCantidad);

        return detalleRepo.save(detalle);
    }

    @Override
    @Transactional
    public DetalleCarrito actualizarCantidad(Long idDetalle, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        DetalleCarrito detalle = detalleRepo.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("El detalle del carrito no existe"));

        Producto producto = detalle.getProducto();
        if (cantidad > producto.getStockActual()) {
            throw new IllegalArgumentException("La cantidad solicitada excede el stock disponible");
        }

        detalle.setCantidad(cantidad);
        detalle.setSubtotal(detalle.getPrecioUnitario() * cantidad);
        return detalleRepo.save(detalle);
    }

    @Override
    @Transactional
    public void eliminarDetalle(Long idDetalle) {
        if (!detalleRepo.existsById(idDetalle)) {
            throw new IllegalArgumentException("El detalle del carrito no existe");
        }
        detalleRepo.deleteById(idDetalle);
    }
}
