package com.example.demo.model.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "OrdenCompra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OrdenCompra")
    private Long idOrdenCompra;

    @ManyToOne
    @JoinColumn(name = "ID_Proveedor", nullable = false)
    private Proveedor proveedor;

    @NotBlank(message = "El número de orden es obligatorio")
    @Column(name = "numero_orden", nullable = false, unique = true, length = 20)
    private String numeroOrden;

    @Column(name = "fecha_orden")
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @Column(name = "fecha_entrega_esperada")
    private LocalDate fechaEntregaEsperada;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal = 0.00;

    @Column(name = "igv", nullable = false)
    private Double igv = 0.00;

    @Column(name = "total", nullable = false)
    private Double total = 0.00;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.Pendiente;

    public enum Estado {
        Pendiente, Confirmada, Recibida, Cancelada
    }

    @Column(name = "condiciones_pago", length = 100)
    private String condicionesPago;

    @Column(name = "usuario_registro")
    private Integer usuarioRegistro;
}
