import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket } from '../model/ticket';
import { API_ENDPOINTS } from '../core/config/api.config';

@Injectable({
  providedIn: 'root',
})
export class AdminTicketsService {
  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${API_ENDPOINTS.tickets}/`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${API_ENDPOINTS.tickets}/eliminar/${id}`);
  }

  resolver(id: number, solucion: string): Observable<Ticket> {
    return this.http.put<Ticket>(`${API_ENDPOINTS.tickets}/resolver/${id}`, { solucion });
  }
}
