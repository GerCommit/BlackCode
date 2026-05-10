package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.DetalleOrdenCompra;

public interface IDetalleOrdenCompraRepository  extends JpaRepository<DetalleOrdenCompra, Long>     {

}
