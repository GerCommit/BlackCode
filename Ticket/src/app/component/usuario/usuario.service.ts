import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Usuario } from '../../model/usuario';
import { API_ENDPOINTS } from '../../core/config/api.config';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  constructor(private http: HttpClient) {}

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(API_ENDPOINTS.usuarios);
  }

  listarUltimosAccesosClientes(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${API_ENDPOINTS.usuarios}/clientes/ultimos-accesos`);
  }

  actualizar(id: number, usuario: any): Observable<any> {
    return this.http.put(`${API_ENDPOINTS.usuarios}/${id}`, usuario);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${API_ENDPOINTS.usuarios}/${id}`);
  }

  solicitarRecuperacion(email: string): Observable<string> {
    const params = new HttpParams().set('email', email);
    return this.http.post(`${API_ENDPOINTS.usuarios}/recuperar-password`, null, {
      params,
      responseType: 'text',
    });
  }

  resetearPassword(token: string, nuevaPassword: string): Observable<string> {
    const params = new HttpParams().set('token', token).set('nuevaPassword', nuevaPassword);
    return this.http.post(`${API_ENDPOINTS.usuarios}/reset-password`, null, {
      params,
      responseType: 'text',
    });
  }
}
