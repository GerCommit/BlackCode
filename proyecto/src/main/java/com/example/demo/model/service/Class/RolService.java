package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.demo.model.entidad.Rol;
import com.example.demo.model.repository.IRolRepository;
import com.example.demo.model.service.Interface.IRolService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class RolService implements IRolService{

    private final IRolRepository repository;

    
    @Override
    public List<Rol> listar() {
        return repository.findAll();
    }
    @Override
    public Rol guardar(Rol rol) {
        return repository.save(rol);
    }
    @Override   
    public Rol obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
