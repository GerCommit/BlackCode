package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByNumeroPedido(String numeroPedido);
    List<Pedido> findByUsuarioIdUsuarioOrderByFechaPedidoDesc(Long idUsuario);
}
