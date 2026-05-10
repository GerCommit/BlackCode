import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import { Usuario } from '../../model/usuario';
import { UsuarioService } from './usuario.service';

@Component({
  selector: 'app-ultimos-accesos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ultimos-accesos.html',
  styleUrls: ['./ultimos-accesos.css'],
})
export class UltimosAccesosComponent implements OnInit {
  clientes: Usuario[] = [];
  cargando = true;
  errorMessage = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUltimosAccesos();
  }

  cargarUltimosAccesos(): void {
    this.cargando = true;
    this.errorMessage = '';

    this.usuarioService.listarUltimosAccesosClientes().subscribe({
      next: (clientes) => {
        this.clientes = clientes;
        this.cargando = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el historial de accesos.';
        this.cargando = false;
      },
    });
  }

  formatearUltimoAcceso(fecha?: Date): string {
    if (!fecha) {
      return 'Sin registros';
    }

    return new Date(fecha).toLocaleString('es-PE', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  }
}
