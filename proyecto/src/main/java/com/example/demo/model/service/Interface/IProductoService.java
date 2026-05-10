package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Producto;

public interface IProductoService {
    List<Producto> listar();
    List<Producto> listarPorCategoria(String categoria);
    Producto guardar(Producto producto);
    Producto obtenerPorId(Long id);
    void eliminar(Long id);
}
