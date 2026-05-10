export interface Pedido {
  idPedido: number;
  idUsuario: number;
  numeroPedido: string;
  fechaRegistro: string;
  estado: 'Pendiente' | 'Pagado' | 'Entregado' | 'Cancelado';
  metodoPago: string;
  total: number;
  envio: {
    idInfoEnvio: number;
    direccion: string;
    distrito: string;
    ciudad: string;
    nombreDestinatario: string;
  };
  detalles: PedidoDetalle[];
}

export interface PedidoDetalle {
  idDetalle: number;
  idProducto: number;
  nombreProducto: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface PedidoResponse {
  idPedido: number;
  numeroPedido: string;
  fechaRegistro: string;
  estado: string;
  metodoPago: string;
  total: number;
  idInfoEnvio: number;
  direccion: string;
  distrito: string;
  ciudad: string;
  nombreDestinatario: string;
  detalles: {
    idDetalle: number;
    idProducto: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
    subtotal: number;
  }[];
}