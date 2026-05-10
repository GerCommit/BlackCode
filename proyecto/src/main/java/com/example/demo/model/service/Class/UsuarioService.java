package com.example.demo.model.service.Class;

import java.util.List;
import java.util.Comparator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.Rol;
import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.IRolRepository;
import com.example.demo.model.repository.IUsuarioRepository;
import com.example.demo.model.service.Interface.IUsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarClientesConUltimoAcceso() {
        return repository.findAll().stream()
                .filter(usuario -> usuario.getRol() != null
                        && "CLIENTE".equalsIgnoreCase(usuario.getRol().getNombreRol()))
                .sorted(Comparator.comparing(
                        Usuario::getUltimaFechaLogin,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuario.getIdUsuario() != null) {
            return actualizarUsuario(usuario);
        }

        return crearUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("El usuario con id " + id + " no existe"));

        usuario.setActivo(false);
        repository.save(usuario);
    }

    private Usuario crearUsuario(Usuario usuario) {
        validarPasswordRequerido(usuario.getPasswordHash());

        if (usuario.getRol() == null || usuario.getRol().getIdRol() == null) {
            usuario.setRol(obtenerRolCliente());
        }

        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setActivo(usuario.getActivo() == null ? true : usuario.getActivo());
        return repository.save(usuario);
    }

    private Usuario actualizarUsuario(Usuario usuario) {
        Usuario usuarioExistente = repository.findById(usuario.getIdUsuario()).orElseThrow(
                () -> new IllegalArgumentException("El usuario con id " + usuario.getIdUsuario() + " no existe"));

        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(usuarioExistente.getPasswordHash());
        } else {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }

        if (usuario.getRol() == null) {
            usuario.setRol(usuarioExistente.getRol());
        }

        if (usuario.getActivo() == null) {
            usuario.setActivo(usuarioExistente.getActivo());
        }

        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(usuarioExistente.getFechaRegistro());
        }

        if (usuario.getUltimaFechaLogin() == null) {
            usuario.setUltimaFechaLogin(usuarioExistente.getUltimaFechaLogin());
        }

        if (usuario.getResetToken() == null) {
            usuario.setResetToken(usuarioExistente.getResetToken());
        }

        return repository.save(usuario);
    }

    private void validarPasswordRequerido(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria.");
        }
    }

    private Rol obtenerRolCliente() {
        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE");

        if (rolCliente == null) {
            throw new IllegalStateException("No existe el rol CLIENTE configurado.");
        }

        return rolCliente;
    }
}
