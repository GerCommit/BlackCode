import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
})
export class Admin {
  constructor(
    private router: Router,
    private authService: AuthService,
    private sessionActivityService: SessionActivityService,
  ) {}

  onLogout(): void {
    this.authService.logout().subscribe(() => {
      this.sessionActivityService.clearTimer();
      this.router.navigate(['/login']);
    });
  }
}
