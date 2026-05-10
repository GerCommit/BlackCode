package com.example.demo.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.entidad.DetalleCarrito;

public interface IDetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    List<DetalleCarrito> findByCarritoIdCarrito(Long idCarrito);
    Optional<DetalleCarrito> findByCarritoIdCarritoAndProductoIdProducto(Long idCarrito, Long idProducto);
}
