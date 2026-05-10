import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { API_ENDPOINTS } from '../core/config/api.config';
import { LoginRequest, RegisterRequest, Usuario } from '../model/usuario';

const AUTH_MESSAGE_KEY = 'auth_message';
const REMEMBERED_USERNAME_KEY = 'remembered_username';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<Usuario | null>;
  public currentUser: Observable<Usuario | null>;

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('usuario');
    this.currentUserSubject = new BehaviorSubject<Usuario | null>(
      storedUser ? JSON.parse(storedUser) : null,
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): Usuario | null {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string, recordarme: boolean): Observable<Usuario> {
    const loginRequest: LoginRequest = { username, password };

    return this.http.post<Usuario>(API_ENDPOINTS.auth, loginRequest).pipe(
      tap((usuario) => {
        this.storeUser(usuario);

        if (recordarme) {
          localStorage.setItem(REMEMBERED_USERNAME_KEY, username);
        } else {
          localStorage.removeItem(REMEMBERED_USERNAME_KEY);
        }
      }),
      catchError((error) => throwError(() => new Error(this.resolveAuthError(error)))),
    );
  }

  register(registerData: RegisterRequest): Observable<Usuario> {
    const nuevoUsuario = {
      username: registerData.username,
      passwordHash: registerData.passwordHash,
      nombres: registerData.nombres,
      apellidos: registerData.apellidos,
      email: registerData.email,
      telefono: registerData.telefono,
    };

    return this.http.post<Usuario>(API_ENDPOINTS.usuarios, nuevoUsuario).pipe(
      catchError((error) => throwError(() => new Error(this.resolveRegisterError(error)))),
    );
  }

  logout(message?: string): Observable<void> {
    return this.http.post(`${API_ENDPOINTS.auth}/logout`, {}, { responseType: 'text' }).pipe(
      map(() => void 0),
      catchError(() => of(void 0)),
      tap(() => this.clearSession(message)),
    );
  }

  clearSession(message?: string): void {
    localStorage.removeItem('usuario');
    this.currentUserSubject.next(null);

    if (message) {
      sessionStorage.setItem(AUTH_MESSAGE_KEY, message);
    } else {
      sessionStorage.removeItem(AUTH_MESSAGE_KEY);
    }
  }

  consumeAuthMessage(): string {
    const message = sessionStorage.getItem(AUTH_MESSAGE_KEY) ?? '';
    sessionStorage.removeItem(AUTH_MESSAGE_KEY);
    return message;
  }

  getRememberedUsername(): string {
    return localStorage.getItem(REMEMBERED_USERNAME_KEY) ?? '';
  }

  storeUser(usuario: Usuario): void {
    localStorage.setItem('usuario', JSON.stringify(usuario));
    this.currentUserSubject.next(usuario);
  }

  isAuthenticated(): boolean {
    return this.currentUserValue !== null;
  }

  hasAnyRole(roles: string[]): boolean {
    const currentRole = this.currentUserValue?.rol?.nombreRol;
    return !!currentRole && roles.includes(currentRole);
  }

  getRedirectRouteForCurrentUser(): string {
    return this.getRedirectRoute(this.currentUserValue);
  }

  getRedirectRoute(usuario: Usuario | null): string {
    if (usuario?.rol?.nombreRol === 'ADMINISTRADOR') {
      return '/admin';
    }

    if (usuario?.rol?.nombreRol === 'CLIENTE') {
      return '/home';
    }

    return '/login';
  }

  private resolveAuthError(error: unknown): string {
    if (!(error instanceof HttpErrorResponse)) {
      return 'No se pudo iniciar sesion.';
    }

    if (typeof error.error === 'string' && error.error.trim()) {
      return error.error;
    }

    if (error.status === 403) {
      return 'La cuenta se encuentra inactiva.';
    }

    if (error.status === 401) {
      return 'Usuario o contrasena incorrectos.';
    }

    return 'No se pudo iniciar sesion.';
  }

  private resolveRegisterError(error: unknown): string {
    if (!(error instanceof HttpErrorResponse)) {
      return 'Error al registrar usuario.';
    }

    if (error.error?.errores) {
      const firstError = Object.values<string>(error.error.errores)[0];
      return firstError || 'Los datos ingresados no son validos.';
    }

    if (typeof error.error?.mensaje === 'string') {
      return error.error.mensaje;
    }

    if (error.status === 409) {
      return 'El username o email ya estan registrados.';
    }

    return 'Error al registrar usuario.';
  }
}
