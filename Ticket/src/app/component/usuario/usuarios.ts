import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// Asegúrate de que esta ruta apunte correctamente a tu servicio
import { UsuarioService } from './usuario.service'; 

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './usuarios.html',
  styleUrls: ['./usuarios.css']
})
export class UsuariosComponent implements OnInit {
  usuarios: any[] = [];
  usuarioSeleccionado: any = null;
  errorMessage = '';
  successMessage = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.listar().subscribe({
      next: (data) => this.usuarios = data,
      error: () => {
        this.errorMessage = 'Error al cargar usuarios';
      }
    });
  }

  editar(usuario: any) {
    // Clonamos el objeto para no afectar la tabla hasta que le den "Guardar"
    this.usuarioSeleccionado = { ...usuario };
  }

  guardarEdicion() {
    if (this.usuarioSeleccionado && this.usuarioSeleccionado.idUsuario) {
      this.usuarioService.actualizar(this.usuarioSeleccionado.idUsuario, this.usuarioSeleccionado)
        .subscribe({
          next: () => {
            this.cargarUsuarios(); // Recargamos la tabla
            this.usuarioSeleccionado = null; // Cerramos el modal
            alert('Usuario actualizado correctamente');
          },
          error: (err) => console.error('Error al actualizar', err)
        });
    }
  }

  // NUEVA FUNCIÓN: Reemplaza a eliminar()
  cambiarEstado(usuario: any) {
    const accion = usuario.activo ? 'desactivar' : 'activar';
    
    if (confirm(`¿Estás seguro de que deseas ${accion} a este usuario?`)) {
      // 1. Clonamos al usuario y le invertimos su estado actual
      const usuarioActualizado = { ...usuario, activo: !usuario.activo };

      // 2. Usamos el endpoint de actualizar
      this.usuarioService.actualizar(usuario.idUsuario, usuarioActualizado).subscribe({
        next: () => {
          this.cargarUsuarios(); // Recarga la tabla para reflejar el cambio de estado
        },
        error: (err) => {
          console.error(`Error al ${accion} el usuario`, err);
          alert('Hubo un error al cambiar el estado del usuario.');
        }
      });
    }
  }

  cancelarEdicion() {
    this.usuarioSeleccionado = null;
  }
}