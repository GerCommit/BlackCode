package com.example.demo.model.service.Class;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.IUsuarioRepository;
import com.example.demo.model.service.Interface.ILoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorUsernameOEmail(String usernameOEmail) {
        return usuarioRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(usernameOEmail, usernameOEmail);
    }

    @Override
    @Transactional
    public Usuario registrarInicioSesion(String usernameOEmail) {
        Usuario usuario = buscarPorUsernameOEmail(usernameOEmail);

        if (usuario == null) {
            return null;
        }

        usuario.setUltimaFechaLogin(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }
}
