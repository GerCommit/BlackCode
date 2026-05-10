package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.entidad.Rol;
import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.IRolRepository;
import com.example.demo.model.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initRolesYAdmin() {
        return args -> {
            Rol rolCliente = asegurarRol("CLIENTE");
            Rol rolAdmin = asegurarRol("ADMINISTRADOR");

            Usuario admin = usuarioRepository.findByUsername("admin");

            if (admin == null) {
                Usuario nuevoAdmin = new Usuario();
                nuevoAdmin.setUsername("admin");
                nuevoAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
                nuevoAdmin.setNombres("Administrador");
                nuevoAdmin.setApellidos("Sistema");
                nuevoAdmin.setEmail("admin@blackcode.com");
                nuevoAdmin.setTelefono("999888777");
                nuevoAdmin.setActivo(true);
                nuevoAdmin.setRol(rolAdmin);

                usuarioRepository.save(nuevoAdmin);
            }

            if (rolCliente == null || rolAdmin == null) {
                throw new IllegalStateException("No se pudieron inicializar los roles base.");
            }
        };
    }

    private Rol asegurarRol(String nombreRol) {
        Rol rol = rolRepository.findByNombreRol(nombreRol);

        if (rol != null) {
            return rol;
        }

        Rol nuevoRol = new Rol();
        nuevoRol.setNombreRol(nombreRol);
        return rolRepository.save(nuevoRol);
    }
}
