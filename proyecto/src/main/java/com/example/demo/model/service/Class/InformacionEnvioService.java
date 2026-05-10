package com.example.demo.model.service.Class;

import com.example.demo.model.entidad.InformacionEnvio;
import com.example.demo.model.repository.IInformacionEnvioRepository;
import com.example.demo.model.service.Interface.IInformacionEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformacionEnvioService implements IInformacionEnvioService {

    private final IInformacionEnvioRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<InformacionEnvio> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public InformacionEnvio guardar(InformacionEnvio informacionEnvio) {
        return repository.save(informacionEnvio);
    }

    @Override
    @Transactional(readOnly = true)
    public InformacionEnvio obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InformacionEnvio> obtenerPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InformacionEnvio> obtenerActivasPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuarioAndActivoTrue(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public InformacionEnvio obtenerPrincipalPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuarioAndEsPrincipalTrue(idUsuario);
    }
}
