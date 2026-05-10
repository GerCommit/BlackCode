package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Rol;

public interface IRolService {
    List<Rol> listar();
    Rol guardar(Rol rol);
    Rol obtenerPorId(Long id);
    void eliminar(Long id);
}
