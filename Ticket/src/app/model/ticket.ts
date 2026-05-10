export interface Ticket{
    idTicket: number
    descripcion: string
    fechaRegistro: string
    estado: string
    solucion?: string;
    usuario?: {idUsuario: number;};
}