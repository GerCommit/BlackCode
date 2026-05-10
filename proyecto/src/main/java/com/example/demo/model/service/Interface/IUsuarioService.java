package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Usuario;

public interface IUsuarioService {
    List<Usuario> listar();
    List<Usuario> listarClientesConUltimoAcceso();
    Usuario guardar(Usuario usuario);
    Usuario obtenerPorId(Long id);
    void eliminar(Long id);
}
