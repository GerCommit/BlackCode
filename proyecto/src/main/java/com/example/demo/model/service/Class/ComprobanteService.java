package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.Comprobante;
import com.example.demo.model.repository.IComprobanteRepository;
import com.example.demo.model.service.Interface.IComprobanteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComprobanteService implements IComprobanteService {

    private final IComprobanteRepository repository;

    @Override
    public List<Comprobante> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Comprobante guardar(Comprobante comprobante) {
        return repository.save(comprobante);
    }

    @Override
    public Comprobante obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
