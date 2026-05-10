import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket } from '../model/ticket';
import { API_ENDPOINTS } from '../core/config/api.config';

@Injectable({
  providedIn: 'root',
})
export class TicketsService {
  constructor(private http: HttpClient) {}

  getTicketsPendientes(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${API_ENDPOINTS.tickets}/pendientes`);
  }

  getTicketsAtendidos(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${API_ENDPOINTS.tickets}/atendidos`);
  }

  guardar(ticket: Partial<Ticket>): Observable<Ticket> {
    return this.http.post<Ticket>(`${API_ENDPOINTS.tickets}/guardar`, ticket);
  }

  crearTicket(ticket: Ticket): Observable<Ticket> {
    return this.http.post<Ticket>(API_ENDPOINTS.tickets, ticket);
  }

  resolverTicket(id: number): Observable<void> {
    return this.http.put<void>(`${API_ENDPOINTS.tickets}/${id}/resolver`, {});
  }
}
