package com.example.demo.model.entidad;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario")
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_Rol", nullable = false)
    private Rol rol;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 6, max = 20, message = "El username debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Solo letras y números sin espacios")
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 10, max = 50, message = "La contraseña debe tener entre 10 y 50 caracteres")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$", 
             message = "La contraseña debe contener letras, números y caracteres especiales")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 3, max = 200, message = "Los nombres deben tener entre 3 y 200 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+((\\s+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*)$", 
             message = "Solo letras, sin espacios al inicio/final ni múltiples espacios")
    @Column(name = "nombres", length = 200)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 3, max = 200, message = "Los apellidos deben tener entre 3 y 200 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+((\\s+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*)$", 
             message = "Solo letras, sin espacios al inicio/final ni múltiples espacios")
    @Column(name = "apellidos", length = 200)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "Debe tener exactamente 8 dígitos")
    @Column(name = "telefono", length = 8)
    private String telefono;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "ultima_fecha_login")
    private LocalDateTime ultimaFechaLogin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "reset_token")
    private String resetToken;
}
