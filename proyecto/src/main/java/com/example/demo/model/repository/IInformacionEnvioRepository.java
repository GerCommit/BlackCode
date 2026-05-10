package com.example.demo.model.repository;

import com.example.demo.model.entidad.InformacionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IInformacionEnvioRepository extends JpaRepository<InformacionEnvio, Long> {
    
    List<InformacionEnvio> findByUsuario_IdUsuario(Long idUsuario);
    
    List<InformacionEnvio> findByUsuario_IdUsuarioAndActivoTrue(Long idUsuario);
    
    InformacionEnvio findByUsuario_IdUsuarioAndEsPrincipalTrue(Long idUsuario);
}
