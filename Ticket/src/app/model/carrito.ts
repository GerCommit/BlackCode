export interface CarritoResponse {
  idCarrito: number;
  idUsuario: number;
  activo: boolean;
}

export interface DetalleCarrito {
  idDetalle: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  producto: {
    idProducto: number;
    nombreProducto: string;
  };
}

export interface Carrito {
  idCarrito: number;
  idUsuario?: number;
  activo?: boolean;
}