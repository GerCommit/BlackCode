package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.entidad.Rol;

public interface IRolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombreRol(String nombreRol);
}
