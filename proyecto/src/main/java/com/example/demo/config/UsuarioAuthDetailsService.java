package com.example.demo.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.service.Interface.ILoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioAuthDetailsService implements UserDetailsService {

    private final ILoginService loginService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = loginService.buscarPorUsernameOEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Credenciales invalidas");
        }

        return new UsuarioAuthDetails(usuario);
    }
}
