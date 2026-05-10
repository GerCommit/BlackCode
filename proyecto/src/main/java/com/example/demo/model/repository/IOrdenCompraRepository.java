package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.OrdenCompra;

public interface IOrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

}
