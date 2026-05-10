package com.example.demo.model.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Informacion_Envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_InfoEnvio")
    private Long idInfoEnvio;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "El nombre del destinatario es obligatorio")
    @Column(name = "nombre_destinatario", nullable = false, length = 150)
    private String nombreDestinatario;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    @Column(name = "telefono_contacto", nullable = false, length = 20)
    private String telefonoContacto;

    @Column(name = "referencia", columnDefinition = "TEXT")
    private String referencia;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}