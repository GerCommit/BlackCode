import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../core/config/api.config';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  constructor(private http: HttpClient) {}

  getUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${API_ENDPOINTS.usuarios}/`);
  }

  getUsuarioPorId(id: number): Observable<any> {
    return this.http.get<any>(`${API_ENDPOINTS.usuarios}/buscar/${id}`);
  }

  guardarUsuario(usuario: any): Observable<any> {
    return this.http.post<any>(`${API_ENDPOINTS.usuarios}/guardar`, usuario);
  }

  actualizarUsuario(id: number, usuario: any): Observable<any> {
    return this.http.put<any>(`${API_ENDPOINTS.usuarios}/actualizar/${id}`, usuario);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete<any>(`${API_ENDPOINTS.usuarios}/eliminar/${id}`);
  }
}
