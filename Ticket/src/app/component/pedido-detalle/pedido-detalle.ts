import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PedidoResponse } from '../../model/pedido';
import { PedidosService } from '../../service/pedidos-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pedido-detalle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedido-detalle.html',
  styleUrl: './pedido-detalle.css',
})
export class PedidoDetalle implements OnInit {
  pedido: PedidoResponse | null = null;
  loading = true;
  error = '';
  cancelando = false;

  constructor(
    private route: ActivatedRoute,
    private pedidosService: PedidosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idPedido = this.route.snapshot.paramMap.get('id');
    if (idPedido) {
      this.cargarDetalle(+idPedido);
    } else {
      this.router.navigate(['/mis-pedidos']);
    }
  }

  cargarDetalle(idPedido: number): void {
    this.loading = true;
    this.pedidosService.obtenerDetallePedido(idPedido).subscribe({
      next: (pedido) => {
        this.pedido = pedido;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar los detalles del pedido';
        this.loading = false;
      },
    });
  }

  cancelarPedido(): void {
    if (!this.pedido) return;
    
    if (!confirm('¿Estás seguro de que deseas cancelar este pedido?')) {
      return;
    }

    this.cancelando = true;
    this.pedidosService.cancelarPedido(this.pedido.idPedido).subscribe({
      next: () => {
        this.pedido!.estado = 'Cancelado';
        this.cancelando = false;
        alert('Pedido cancelado correctamente');
      },
      error: () => {
        this.cancelando = false;
        alert('No se pudo cancelar el pedido');
      },
    });
  }

  volver(): void {
    this.router.navigate(['/mis-pedidos']);
  }

  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'Pendiente':
        return 'estado-pendiente';
      case 'Pagado':
        return 'estado-pagado';
      case 'Entregado':
        return 'estado-entregado';
      case 'Cancelado':
        return 'estado-cancelado';
      default:
        return '';
    }
  }

  get puedeCancelar(): boolean {
    return this.pedido?.estado === 'Pendiente';
  }
}