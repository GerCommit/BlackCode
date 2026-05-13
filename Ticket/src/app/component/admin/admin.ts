import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';
import { Usuario } from '../../model/usuario';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
})
export class Admin {
  usuario: Usuario | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private sessionActivityService: SessionActivityService,
  ) {
    this.usuario = this.authService.currentUserValue;
  }

  get nombreAdmin(): string {
    if (this.usuario?.nombres) {
      return this.usuario.nombres.charAt(0).toUpperCase();
    }
    return 'A';
  }

  get nombreCompleto(): string {
    if (this.usuario?.nombres && this.usuario?.apellidos) {
      return `${this.usuario.nombres} ${this.usuario.apellidos}`;
    }
    return 'Administrador';
  }

  onLogout(): void {
    this.authService.logout().subscribe(() => {
      this.sessionActivityService.clearTimer();
      this.router.navigate(['/login']);
    });
  }
}
