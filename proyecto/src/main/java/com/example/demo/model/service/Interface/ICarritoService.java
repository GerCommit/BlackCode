package com.example.demo.model.service.Interface;

import com.example.demo.model.entidad.Carrito;

public interface ICarritoService {
    Carrito obtenerCarritoActivo(Long idUsuario);
    Carrito crearCarrito(Long idUsuario);
}
