package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.Comprobante;

public interface IComprobanteRepository extends JpaRepository<Comprobante, Long> {

}
