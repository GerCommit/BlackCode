package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Pedido;

public interface IPedidoService {
    List<Pedido> listar();
    List<Pedido> listarPorUsuario(Long idUsuario);
    Pedido guardar(Pedido pedido);
    Pedido obtenerPorId(Long id);
    Pedido crearDesdeCarrito(Long idUsuario, Long idInfoEnvio, Pedido.MetodoPago metodoPago);
    Pedido cancelarPedido(Long idPedido, Long idUsuario);
    Pedido actualizarEstado(Long idPedido, Pedido.Estado estado);
    void eliminar(Long id);
}
