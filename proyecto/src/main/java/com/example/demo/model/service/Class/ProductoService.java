package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.Producto;
import com.example.demo.model.repository.IProductoRepository;
import com.example.demo.model.service.Interface.IProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final IProductoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repository.findAllByOrderByPrecioVentaAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(String categoria) {
        return repository.findByCategoriaNombreCategoriaIgnoreCaseOrderByPrecioVentaAsc(categoria);
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El producto con id " + id + " no existe");
        }
        repository.deleteById(id);
    }
}
