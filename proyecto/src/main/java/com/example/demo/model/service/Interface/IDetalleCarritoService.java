package com.example.demo.model.service.Interface;

import com.example.demo.model.entidad.DetalleCarrito;

public interface IDetalleCarritoService {

    DetalleCarrito agregarProducto(Long idUsuario, Long idProducto, Integer cantidad, Double precio);
    DetalleCarrito actualizarCantidad(Long idDetalle, Integer cantidad);

    void eliminarDetalle(Long idDetalle);
}
