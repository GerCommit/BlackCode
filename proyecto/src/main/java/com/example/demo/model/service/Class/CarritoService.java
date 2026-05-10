package com.example.demo.model.service.Class;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.Carrito;
import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.ICarritoRepository;
import com.example.demo.model.repository.IUsuarioRepository;
import com.example.demo.model.service.Interface.ICarritoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService implements ICarritoService {

    private final ICarritoRepository repository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public Carrito obtenerCarritoActivo(Long idUsuario) {
        return repository.findByUsuarioIdUsuarioAndActivo(idUsuario, true)
                .orElseGet(() -> crearCarrito(idUsuario));
    }

    @Override
    @Transactional
    public Carrito crearCarrito(Long idUsuario) {
        Carrito carrito = new Carrito();
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        carrito.setUsuario(usuario);
        carrito.setActivo(true);
        return repository.save(carrito);
    }
}
