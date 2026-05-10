package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.Proveedor;
import com.example.demo.model.repository.IProveedorRepository;
import com.example.demo.model.service.Interface.IProveedorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService implements IProveedorService {

    private final IProveedorRepository repository;

    @Override
    public List<Proveedor> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Proveedor guardar(Proveedor proveedor) {
        return repository.save(proveedor);
    }

    @Override
    public Proveedor obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
