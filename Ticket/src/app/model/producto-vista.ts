export interface ProductoVista {
  idProducto: number;
  nombre: string;
  precio: number;
  material: string;
  talla: string;
  marca: string;
  descripcion: string;
  imagenUrl: string;
  stockActual: number | null;
}