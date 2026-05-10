package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.DetalleOrdenCompra;
import com.example.demo.model.repository.IDetalleOrdenCompraRepository;
import com.example.demo.model.service.Interface.IDetalleOrdenCompraService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleOrdenCompraService implements IDetalleOrdenCompraService {

    private final IDetalleOrdenCompraRepository repository;

    @Override
    public List<DetalleOrdenCompra> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public DetalleOrdenCompra guardar(DetalleOrdenCompra detalleOrden) {
        return repository.save(detalleOrden);
    }

    @Override
    public DetalleOrdenCompra obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
