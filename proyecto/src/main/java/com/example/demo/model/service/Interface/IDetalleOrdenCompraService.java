package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.DetalleOrdenCompra;

public interface IDetalleOrdenCompraService {
    List<DetalleOrdenCompra> listar();
    DetalleOrdenCompra guardar(DetalleOrdenCompra detalleOrden);
    DetalleOrdenCompra obtenerPorId(Long id);
    void eliminar(Long id);
}
