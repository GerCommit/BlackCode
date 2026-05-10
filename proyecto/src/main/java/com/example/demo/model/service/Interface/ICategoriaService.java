package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Categoria;

public interface ICategoriaService {
    List<Categoria> listar();
    Categoria guardar(Categoria categoria);
    Categoria obtenerPorId(Long id);
    void eliminar(Long id);
}
