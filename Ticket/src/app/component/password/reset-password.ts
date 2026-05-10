import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../usuario/usuario.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPasswordComponent implements OnInit {
  token: string = '';
  nuevaPassword: string = '';
  confirmarPassword: string = '';
  
  loading: boolean = false;
  tokenValido: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    // Extraemos el token de la URL: ?token=xxxx-xxxx
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      if (this.token) {
        this.tokenValido = true;
      } else {
        this.errorMessage = 'El enlace de recuperación no es válido.';
      }
    });
  }

  onResetPassword(): void {
    if (this.nuevaPassword !== this.confirmarPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    if (this.nuevaPassword.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.usuarioService.resetearPassword(this.token, this.nuevaPassword).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.successMessage = '¡Contraseña actualizada! Redirigiendo al login...';
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'El enlace ha expirado o ya fue utilizado.';
      }
    });
  }
}