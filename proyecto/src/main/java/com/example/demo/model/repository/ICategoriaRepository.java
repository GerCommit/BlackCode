 package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

}
