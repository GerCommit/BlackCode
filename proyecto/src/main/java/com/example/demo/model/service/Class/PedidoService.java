package com.example.demo.model.service.Class;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entidad.Carrito;
import com.example.demo.model.entidad.DetalleCarrito;
import com.example.demo.model.entidad.DetallePedido;
import com.example.demo.model.entidad.InformacionEnvio;
import com.example.demo.model.entidad.Pedido;
import com.example.demo.model.entidad.Producto;
import com.example.demo.model.entidad.Usuario;
import com.example.demo.model.repository.IDetalleCarritoRepository;
import com.example.demo.model.repository.IDetallePedidoRepository;
import com.example.demo.model.repository.IInformacionEnvioRepository;
import com.example.demo.model.repository.IPedidoRepository;
import com.example.demo.model.repository.IProductoRepository;
import com.example.demo.model.repository.IUsuarioRepository;
import com.example.demo.model.service.Interface.IPedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService implements IPedidoService {

    private final IPedidoRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IInformacionEnvioRepository informacionEnvioRepository;
    private final IDetalleCarritoRepository detalleCarritoRepository;
    private final IDetallePedidoRepository detallePedidoRepository;
    private final IProductoRepository productoRepository;
    private final CarritoService carritoService;
    private final EmailService emailService;

    @Override
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @Override
    public List<Pedido> listarPorUsuario(Long idUsuario) {
        return repository.findByUsuarioIdUsuarioOrderByFechaPedidoDesc(idUsuario);
    }

    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isBlank()) {
            pedido.setNumeroPedido(generarNumeroPedidoUnico());
        }
        if (pedido.getMetodoPago() == null) {
            throw new IllegalArgumentException("Debe seleccionar un método de pago");
        }
        return repository.save(pedido);
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }

    @Override
    @Transactional
    public Pedido crearDesdeCarrito(Long idUsuario, Long idInfoEnvio, Pedido.MetodoPago metodoPago) {
        if (metodoPago == null) {
            throw new IllegalArgumentException("Debe seleccionar un método de pago");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        InformacionEnvio info = informacionEnvioRepository.findById(idInfoEnvio)
                .orElseThrow(() -> new IllegalArgumentException("Información de envío no encontrada"));

        Carrito carrito = carritoService.obtenerCarritoActivo(idUsuario);
        List<DetalleCarrito> detalles = carrito.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        double subtotal = 0.0;
        List<DetallePedido> detallePedidos = new ArrayList<>();

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setInformacionEnvio(info);
        pedido.setNumeroPedido(generarNumeroPedidoUnico());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setMetodoPago(metodoPago);
        pedido.setEstado(Pedido.Estado.Pendiente);

        for (DetalleCarrito dc : detalles) {
            Producto producto = productoRepository.findById(dc.getProducto().getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            if (dc.getCantidad() > producto.getStockActual()) {
                throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombreProducto());
            }

            double precio = dc.getPrecioUnitario() == null ? 0.0 : dc.getPrecioUnitario();
            double sub = precio * dc.getCantidad();
            subtotal += sub;

            DetallePedido dp = new DetallePedido();
            dp.setPedido(pedido);
            dp.setProducto(producto);
            dp.setCantidad(dc.getCantidad());
            dp.setPrecioUnitario(precio);
            dp.setDescuento(0.0);
            dp.setSubtotal(sub);
            detallePedidos.add(dp);

            producto.setStockActual(producto.getStockActual() - dc.getCantidad());
            productoRepository.save(producto);
        }

        double igv = subtotal * 0.18;
        pedido.setSubtotal(subtotal);
        pedido.setIgv(igv);
        pedido.setTotal(subtotal + igv);

        Pedido guardado = repository.save(pedido);
        detallePedidos.forEach(d -> d.setPedido(guardado));
        detallePedidoRepository.saveAll(detallePedidos);

        detalleCarritoRepository.deleteAll(detalles);

        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            enviarCorreoConfirmacion(usuario, guardado);
        }

        return guardado;
    }

    @Override
    @Transactional
    public Pedido cancelarPedido(Long idPedido, Long idUsuario) {
        Pedido pedido = obtenerPorId(idPedido);
        if (!pedido.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para cancelar este pedido");
        }
        if (pedido.getEstado() != Pedido.Estado.Pendiente) {
            throw new IllegalArgumentException("Solo se puede cancelar un pedido pendiente");
        }

        pedido.setEstado(Pedido.Estado.Cancelado);
        return repository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Long idPedido, Pedido.Estado estado) {
        Pedido pedido = obtenerPorId(idPedido);
        if (estado == null) {
            throw new IllegalArgumentException("Estado inválido");
        }

        Pedido.Estado actual = pedido.getEstado();
        boolean transicionValida =
                (actual == Pedido.Estado.Pendiente && (estado == Pedido.Estado.Pagado || estado == Pedido.Estado.Cancelado)) ||
                (actual == Pedido.Estado.Pagado && estado == Pedido.Estado.Entregado);

        if (!transicionValida) {
            throw new IllegalArgumentException("Transición de estado no permitida");
        }

        pedido.setEstado(estado);
        return repository.save(pedido);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private String generarNumeroPedidoUnico() {
        String numero;
        do {
            numero = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (repository.existsByNumeroPedido(numero));
        return numero;
    }

    private void enviarCorreoConfirmacion(Usuario usuario, Pedido pedido) {
        try {
            emailService.enviarCorreoConfirmacionPedido(
                    usuario.getEmail(),
                    pedido.getNumeroPedido(),
                    pedido.getTotal(),
                    pedido.getEstado().name()
            );
        } catch (Exception ignored) {
            // El flujo del pedido no debe fallar por correo.
        }
    }
}
