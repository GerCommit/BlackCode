import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AdminTicketsService } from '../../service/admin-tickets-service';
import { Ticket } from '../../model/ticket';

@Component({
  selector: 'app-admin-tickets-components',
  imports: [CommonModule],
  templateUrl: './admin-tickets-components.html',
  styleUrl: './admin-tickets-components.css',
})
export class AdminTicketsComponents implements OnInit{

  tickets: Ticket[] = [];
  ticketSeleccionado?: Ticket;

  constructor(private service: AdminTicketsService) {}

  ngOnInit(): void {
    this.cargarTickets();
  }

  cargarTickets() {
    this.service.listarTodos().subscribe(data => this.tickets = data);
  }

  seleccionar(ticket: Ticket) {
    this.ticketSeleccionado = ticket;
  }

  eliminarSeleccionado() {
    if (!this.ticketSeleccionado) return;
    this.service.eliminar(this.ticketSeleccionado!.idTicket!).subscribe(() => {
      this.cargarTickets();
      this.ticketSeleccionado = undefined;
    });
  }

  verDetalles() {
    if (!this.ticketSeleccionado) return;
    window.location.href = `/admin/detalle/${this.ticketSeleccionado.idTicket}`;
  }
}
