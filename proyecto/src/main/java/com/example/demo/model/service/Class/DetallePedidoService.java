package com.example.demo.model.service.Class;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.entidad.DetallePedido;
import com.example.demo.model.repository.IDetallePedidoRepository;
import com.example.demo.model.service.Interface.IDetallePedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetallePedidoService implements IDetallePedidoService {

    private final IDetallePedidoRepository repository;

    @Override
    public List<DetallePedido> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public DetallePedido guardar(DetallePedido detallePedido) {
        return repository.save(detallePedido);
    }

    @Override
    public DetallePedido obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
