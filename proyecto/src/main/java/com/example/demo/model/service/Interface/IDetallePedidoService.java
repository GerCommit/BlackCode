package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.DetallePedido;

public interface IDetallePedidoService {
    List<DetallePedido> listar();
    DetallePedido guardar(DetallePedido detallePedido);
    DetallePedido obtenerPorId(Long id);
    void eliminar(Long id);
}
