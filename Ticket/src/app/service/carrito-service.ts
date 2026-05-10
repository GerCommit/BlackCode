import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../core/config/api.config';
import { ProductoApi } from '../model/producto-api';

export interface CarritoResponse {
  idCarrito: number;
  idUsuario: number;
  activo: boolean;
}

export interface DetalleResponse {
  idDetalle: number;
  idProducto: number;
  nombreProducto: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface EnvioResponse {
  idInfoEnvio: number;
  nombreDestinatario: string;
  direccion: string;
  ciudad: string;
  distrito: string;
  telefono: string;
  esPrincipal: boolean;
}

export type MetodoPago = 'Efectivo' | 'Tarjeta' | 'Transferencia' | 'Yape' | 'Plin';

export interface CheckoutRequest {
  idUsuario: number;
  idInfoEnvio: number;
  metodoPago: MetodoPago;
}

@Injectable({ providedIn: 'root' })
export class CarritoService {
  constructor(private http: HttpClient) {}

  obtenerCarrito(idUsuario: number): Observable<CarritoResponse> {
    return this.http.get<CarritoResponse>(`${API_ENDPOINTS.carrito}/usuario/${idUsuario}`);
  }

  obtenerDetalles(idUsuario: number): Observable<DetalleResponse[]> {
    return this.http.get<DetalleResponse[]>(`${API_ENDPOINTS.carrito}/detalles/${idUsuario}`);
  }

  agregarProducto(idUsuario: number, idProducto: number, cantidad: number, precio: number): Observable<any> {
    const params = new HttpParams()
      .set('idUsuario', idUsuario.toString())
      .set('idProducto', idProducto.toString())
      .set('cantidad', cantidad.toString())
      .set('precio', precio.toString());
    
    return this.http.post<any>(
      `${API_ENDPOINTS.detalleCarrito}/agregar`,
      null,
      { params }
    );
  }

  eliminarDetalle(idDetalle: number): Observable<void> {
    return this.http.delete<void>(`${API_ENDPOINTS.detalleCarrito}/eliminar/${idDetalle}`);
  }

  listarProductos(): Observable<ProductoApi[]> {
    return this.http.get<ProductoApi[]>(`${API_ENDPOINTS.productos}/`);
  }

  listarProductosPorCategoria(categoria: string): Observable<ProductoApi[]> {
    return this.http.get<ProductoApi[]>(`${API_ENDPOINTS.productos}/categoria/${encodeURIComponent(categoria)}`);
  }

  obtenerEnviosActivos(idUsuario: number): Observable<EnvioResponse[]> {
    return this.http.get<EnvioResponse[]>(`${API_ENDPOINTS.envios}/usuario/${idUsuario}/activas`);
  }

  obtenerMetodosPago(): Observable<string[]> {
    return this.http.get<string[]>(`${API_ENDPOINTS.pedidos}/metodos-pago`);
  }

  registrarPedido(payload: CheckoutRequest): Observable<unknown> {
    return this.http.post(`${API_ENDPOINTS.pedidos}/checkout`, payload);
  }
}