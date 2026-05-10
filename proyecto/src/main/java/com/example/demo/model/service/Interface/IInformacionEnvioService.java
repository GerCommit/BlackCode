package com.example.demo.model.service.Interface;

import com.example.demo.model.entidad.InformacionEnvio;
import java.util.List;

public interface IInformacionEnvioService {
    
    List<InformacionEnvio> listar();
    
    InformacionEnvio guardar(InformacionEnvio informacionEnvio);
    
    InformacionEnvio obtenerPorId(Long id);
    
    void eliminar(Long id);
    
    List<InformacionEnvio> obtenerPorUsuario(Long idUsuario);
    
    List<InformacionEnvio> obtenerActivasPorUsuario(Long idUsuario);
    
    InformacionEnvio obtenerPrincipalPorUsuario(Long idUsuario);
}