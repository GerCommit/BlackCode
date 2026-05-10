package com.example.demo.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entidad.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaIdCategoria(Long idCategoria);
    List<Producto> findByCategoriaNombreCategoriaIgnoreCase(String nombreCategoria);
    List<Producto> findAllByOrderByPrecioVentaAsc();
    List<Producto> findByCategoriaNombreCategoriaIgnoreCaseOrderByPrecioVentaAsc(String nombreCategoria);
}
