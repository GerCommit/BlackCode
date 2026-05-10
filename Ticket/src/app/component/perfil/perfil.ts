import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { AuthService } from '../../service/auth-service';
import { UsuarioService } from '../usuario/usuario.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css'],
})
export class PerfilComponent implements OnInit {
  perfilForm!: FormGroup;
  usuarioActual: any = null;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.currentUserValue;
    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    this.usuarioActual = usuario;

    this.perfilForm = this.formBuilder.group({
      nombres: [this.usuarioActual.nombres, [Validators.required]],
      apellidos: [this.usuarioActual.apellidos, [Validators.required]],
      telefono: [this.usuarioActual.telefono, [Validators.required]],
      password: ['', [Validators.minLength(6)]],
    });
  }

  guardarCambios(): void {
    if (this.perfilForm.invalid) {
      this.errorMessage = 'Por favor, verifica que los campos sean correctos.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formValues = this.perfilForm.value;
    const usuarioActualizado: Record<string, unknown> = {
      ...this.usuarioActual,
      nombres: formValues.nombres,
      apellidos: formValues.apellidos,
      telefono: formValues.telefono,
    };

    if (formValues.password) {
      usuarioActualizado['passwordHash'] = formValues.password;
    }

    this.usuarioService.actualizar(this.usuarioActual.idUsuario, usuarioActualizado).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage = 'Perfil actualizado correctamente.';
        this.authService.storeUser(res);
        this.usuarioActual = res;
        this.perfilForm.get('password')?.setValue('');
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Error al guardar los cambios.';
      },
    });
  }
}
