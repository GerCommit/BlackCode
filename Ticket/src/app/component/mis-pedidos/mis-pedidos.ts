import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PedidoResponse } from '../../model/pedido';
import { PedidosService } from '../../service/pedidos-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mis-pedidos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-pedidos.html',
  styleUrl: './mis-pedidos.css',
})
export class MisPedidos implements OnInit {
  pedidos: PedidoResponse[] = [];
  loading = true;
  error = '';

  constructor(
    private pedidosService: PedidosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const usuarioString = localStorage.getItem('usuario');
    if (usuarioString) {
      const usuario = JSON.parse(usuarioString) as { idUsuario: number };
      this.cargarPedidos(usuario.idUsuario);
    } else {
      this.router.navigate(['/login']);
    }
  }

  cargarPedidos(idUsuario: number): void {
    this.loading = true;
    this.pedidosService.obtenerMisPedidos(idUsuario).subscribe({
      next: (pedidos) => {
        this.pedidos = pedidos.sort(
          (a, b) =>
            new Date(b.fechaRegistro).getTime() -
            new Date(a.fechaRegistro).getTime()
        );
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar los pedidos';
        this.loading = false;
      },
    });
  }

  verDetalle(idPedido: number): void {
    this.router.navigate(['/pedido', idPedido]);
  }

  volver(): void {
    this.router.navigate(['/home']);
  }

  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'short',
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
}