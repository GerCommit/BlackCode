package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.UsuarioAuthDetails;
import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.IUsuarioRepository;
import com.example.demo.model.service.Class.EmailService;
import com.example.demo.model.service.Interface.IUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UsuarioController {

    private final IUsuarioService service;
    private final EmailService emailService;
    private final IUsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Usuario>> cargarUsuarios(Authentication authentication) {
        if (!esAdministrador(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/clientes/ultimos-accesos")
    public ResponseEntity<List<Usuario>> cargarUltimosAccesosClientes(Authentication authentication) {
        if (!esAdministrador(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(service.listarClientesConUltimoAcceso());
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id, Authentication authentication) {
        if (!puedeAccederUsuario(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario usuario = service.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario,
            Authentication authentication) {
        if (!puedeAccederUsuario(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario existente = service.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.setIdUsuario(id);

        if (!esAdministrador(authentication)) {
            usuario.setRol(existente.getRol());
            usuario.setActivo(existente.getActivo());
        }

        return ResponseEntity.ok(service.guardar(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        if (!esAdministrador(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> solicitarRecuperacion(@RequestParam String email) {
        Usuario usuario = repository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace de recuperacion.");
        }

        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        repository.save(usuario);

        emailService.enviarCorreoRecuperacion(email, token);

        return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace de recuperacion.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetearPassword(@RequestParam String token, @RequestParam String nuevaPassword) {
        Usuario usuario = repository.findByResetToken(token);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("El token es invalido o ha expirado.");
        }

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuario.setResetToken(null);
        repository.save(usuario);

        return ResponseEntity.ok("Contrasena actualizada correctamente.");
    }

    private boolean esAdministrador(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                        .anyMatch(authority -> "ROLE_ADMINISTRADOR".equals(authority.getAuthority()));
    }

    private boolean puedeAccederUsuario(Authentication authentication, Long idUsuario) {
        if (esAdministrador(authentication)) {
            return true;
        }

        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioAuthDetails details)) {
            return false;
        }

        return details.getUsuario().getIdUsuario().equals(idUsuario);
    }
}
