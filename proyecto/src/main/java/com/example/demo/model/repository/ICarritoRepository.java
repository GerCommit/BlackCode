package com.example.demo.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.entidad.Carrito;

public interface ICarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdUsuarioAndActivo(Long idUsuario, Boolean activo);
}
