import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../core/config/api.config';
import { PedidoResponse } from '../model/pedido';

@Injectable({
  providedIn: 'root',
})
export class PedidosService {
  constructor(private http: HttpClient) {}

  obtenerMisPedidos(idUsuario: number): Observable<PedidoResponse[]> {
    return this.http.get<PedidoResponse[]>(`${API_ENDPOINTS.pedidos}/usuario/${idUsuario}`);
  }

  obtenerDetallePedido(idPedido: number): Observable<PedidoResponse> {
    return this.http.get<PedidoResponse>(`${API_ENDPOINTS.pedidos}/${idPedido}`);
  }

  cancelarPedido(idPedido: number): Observable<void> {
    return this.http.put<void>(`${API_ENDPOINTS.pedidos}/${idPedido}/cancelar`, {});
  }
}