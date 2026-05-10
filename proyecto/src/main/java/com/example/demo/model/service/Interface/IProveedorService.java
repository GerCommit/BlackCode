package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Proveedor;

public interface IProveedorService {
    List<Proveedor> listar();
    Proveedor guardar(Proveedor proveedor);
    Proveedor obtenerPorId(Long id);
    void eliminar(Long id);
}
