package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    Usuario findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    Usuario findByEmail(String email);

    Usuario findByResetToken(String resetToken);
}
