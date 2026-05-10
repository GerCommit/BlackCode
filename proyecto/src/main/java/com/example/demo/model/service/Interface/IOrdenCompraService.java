package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.OrdenCompra;

public interface IOrdenCompraService {
    List<OrdenCompra> listar();
    OrdenCompra guardar(OrdenCompra ordenCompra);
    OrdenCompra obtenerPorId(Long id);
    void eliminar(Long id);
}
