package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Comprobante;

public interface IComprobanteService {
    List<Comprobante> listar();
    Comprobante guardar(Comprobante comprobante);
    Comprobante obtenerPorId(Long id);
    void eliminar(Long id);
}
