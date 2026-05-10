import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import {
  CarritoService,
  CheckoutRequest,
  DetalleResponse,
  EnvioResponse,
  MetodoPago,
} from '../../service/carrito-service';

@Component({
  selector: 'app-carrito',
  templateUrl: './carrito.html',
  styleUrls: ['./carrito.css'],
  standalone: true,
  imports: [RouterLink]
})
export class Carrito implements OnInit {
  idUsuario = 0;
  detalles: DetalleResponse[] = [];
  envios: EnvioResponse[] = [];

  metodoPagoSeleccionado: MetodoPago = 'Efectivo';
  envioSeleccionadoId: number | null = null;

  subtotal = 0;
  igvTotal = 0;
  total = 0;

  registeringPedido = false;
  errorMessage = '';

  metodosPago: MetodoPago[] = [];

  constructor(private service: CarritoService, private router: Router) {}

  ngOnInit(): void {
    const usuarioString = localStorage.getItem('usuario');
    if (usuarioString) {
      const usuario = JSON.parse(usuarioString) as { idUsuario: number };
      this.idUsuario = usuario.idUsuario;
    }

    if (this.idUsuario <= 0) {
      this.router.navigate(['/login']);
      return;
    }

    this.cargarMetodosPago();
    this.cargarCarrito();
    this.cargarEnvios();
  }

  cargarMetodosPago(): void {
    this.service.obtenerMetodosPago().subscribe({
      next: (metodos) => {
        this.metodosPago = metodos as MetodoPago[];
        if (metodos.length > 0) {
          this.metodoPagoSeleccionado = metodos[0] as MetodoPago;
        }
      },
      error: () => {
        this.metodosPago = ['Efectivo', 'Tarjeta', 'Transferencia', 'Yape', 'Plin'];
      }
    });
  }

cargarCarrito(): void {
    this.service
      .obtenerCarrito(this.idUsuario)
      .pipe(switchMap(() => {
        return this.service.obtenerDetalles(this.idUsuario);
      }))
      .subscribe((detalles) => {
        this.detalles = detalles;
        this.calcularResumen();
      });
  }

  cargarEnvios(): void {
    this.service.obtenerEnviosActivos(this.idUsuario).subscribe((envios) => {
      this.envios = envios;
      if (envios.length > 0) {
        const principal = envios.find((e) => e.esPrincipal);
        this.envioSeleccionadoId = principal?.idInfoEnvio ?? envios[0].idInfoEnvio;
      }
    });
  }

  onEnvioChange(value: string): void {
    this.envioSeleccionadoId = value ? Number(value) : null;
  }

  onMetodoPagoChange(value: string): void {
    this.metodoPagoSeleccionado = value as MetodoPago;
  }

calcularResumen(): void {
    this.subtotal = this.detalles.reduce((sum, item) => sum + item.subtotal, 0);
    this.igvTotal = this.redondear2(this.subtotal * 0.18);
    this.total = this.redondear2(this.subtotal + this.igvTotal);
  }

  eliminar(detalleId: number): void {
    this.service.eliminarDetalle(detalleId).subscribe({
      next: () => {
        this.detalles = this.detalles.filter((d) => d.idDetalle !== detalleId);
        this.calcularResumen();
      },
      error: () => {
        this.errorMessage = 'Error al eliminar el producto';
      }
    });
  }

  registrarPedido(): void {
    if (this.detalles.length === 0) {
      this.errorMessage = 'No hay productos en el carrito.';
      return;
    }
    if (!this.envioSeleccionadoId) {
      this.errorMessage = 'Debes seleccionar una dirección de envío.';
      return;
    }
    this.errorMessage = '';

    const payload: CheckoutRequest = {
      idUsuario: this.idUsuario,
      idInfoEnvio: this.envioSeleccionadoId,
      metodoPago: this.metodoPagoSeleccionado,
    };

    this.registeringPedido = true;
    this.service.registrarPedido(payload).subscribe({
      next: () => {
        alert('Pedido registrado correctamente.');
        this.detalles = [];
        this.calcularResumen();
        this.router.navigate(['/mis-pedidos']);
      },
      error: (err) => {
        this.registeringPedido = false;
        this.errorMessage = err?.error?.message ?? 'No se pudo registrar el pedido.';
      }
    });
  }

  volverTienda(): void {
    this.router.navigate(['/home']);
  }

  private redondear2(valor: number): number {
    return Math.round(valor * 100) / 100;
  }
}
