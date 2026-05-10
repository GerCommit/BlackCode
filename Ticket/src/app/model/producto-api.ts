export interface ProductoApi {
  idProducto: number;
  idCategoria: number;
  codigoProducto: string;
  nombreProducto: string;
  descripcion: string;
  material: string;
  color: string;
  tallaMedida: string;
  precioVenta: number;
  precioCompra: number;
  stockActual: number;
  stockMinimo: number;
  marca: string;
  imagenUrl: string;
  activo: boolean;
}