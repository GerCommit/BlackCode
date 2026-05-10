// src/app/models/usuario.model.ts

export interface Rol {
  idRol: number;
  nombreRol: string;
}

export interface Usuario {
  idUsuario?: number;
  rol: Rol;
  username: string;
  passwordHash?: string;
  nombres: string;
  apellidos: string;
  email: string;
  telefono: string;
  activo: boolean;
  fechaRegistro?: Date;
  ultimaFechaLogin?: Date;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  passwordHash: string;
  nombres: string;
  apellidos: string;
  email: string;
  telefono: string;
}
