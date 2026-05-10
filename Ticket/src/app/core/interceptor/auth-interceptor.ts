import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const sessionActivityService = inject(SessionActivityService);

  const requestWithCredentials = req.clone({
    withCredentials: true,
  });

  return next(requestWithCredentials).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && authService.isAuthenticated()) {
        sessionActivityService.clearTimer();
        authService.clearSession('Tu sesion expiro. Inicia sesion nuevamente.');
        router.navigate(['/login']);
      }

      return throwError(() => error);
    }),
  );
};
