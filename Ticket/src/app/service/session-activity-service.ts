import { Injectable, NgZone } from '@angular/core';

import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root',
})
export class SessionActivityService {
  private readonly inactivityTimeoutMs = 30 * 60 * 1000;
  private readonly events = ['click', 'keydown', 'mousemove', 'scroll', 'touchstart'];
  private timerId: ReturnType<typeof setTimeout> | null = null;
  private initialized = false;

  private readonly activityHandler = () => {
    if (!this.authService.isAuthenticated()) {
      this.clearTimer();
      return;
    }

    this.resetTimer();
  };

  constructor(
    private authService: AuthService,
    private ngZone: NgZone,
  ) {}

  initialize(): void {
    if (this.initialized || typeof window === 'undefined') {
      return;
    }

    this.initialized = true;

    this.ngZone.runOutsideAngular(() => {
      for (const eventName of this.events) {
        window.addEventListener(eventName, this.activityHandler, true);
      }
    });

    if (this.authService.isAuthenticated()) {
      this.resetTimer();
    }
  }

  resetTimer(): void {
    this.clearTimer();

    if (!this.authService.isAuthenticated()) {
      return;
    }

    this.timerId = setTimeout(() => {
      this.ngZone.run(() => {
        this.authService.clearSession('Tu sesion expiro por inactividad.');
        window.location.assign('/login');
      });
    }, this.inactivityTimeoutMs);
  }

  clearTimer(): void {
    if (this.timerId) {
      clearTimeout(this.timerId);
      this.timerId = null;
    }
  }
}
