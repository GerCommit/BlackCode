export const API_BASE_URL = 'http://localhost:8080';

export const API_ENDPOINTS = {
  auth: `${API_BASE_URL}/api/login`,
  usuarios: `${API_BASE_URL}/api/usuarios`,
  tickets: `${API_BASE_URL}/api/tickets`,
  carrito: `${API_BASE_URL}/api/carrito`,
  detalleCarrito: `${API_BASE_URL}/api/detalle-carrito`,
  productos: `${API_BASE_URL}/api/productos`,
  envios: `${API_BASE_URL}/api/envios`,
  pedidos: `${API_BASE_URL}/api/pedidos`,
} as const;
