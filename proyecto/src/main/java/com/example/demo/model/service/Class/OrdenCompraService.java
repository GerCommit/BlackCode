package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.OrdenCompra;
import com.example.demo.model.repository.IOrdenCompraRepository;
import com.example.demo.model.service.Interface.IOrdenCompraService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenCompraService implements IOrdenCompraService {

    private final IOrdenCompraRepository repository;

    @Override
    public List<OrdenCompra> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public OrdenCompra guardar(OrdenCompra ordenCompra) {
        return repository.save(ordenCompra);
    }

    @Override
    public OrdenCompra obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
