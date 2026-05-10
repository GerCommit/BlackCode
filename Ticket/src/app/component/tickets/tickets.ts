import { Component, OnInit } from '@angular/core';
import { Ticket } from '../../model/ticket';
import { TicketsService } from '../../service/tickets-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-tickets',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './tickets.html',
  styleUrl: './tickets.css',
})
export class Tickets implements OnInit{

  ticketsPendientes: Ticket[] = [];
  ticketsAtendidos: Ticket[] = [];
  nuevoTicket = {
    descripcion: ""
  };
  vistaActual: 'pendientes' | 'atendidos' | 'nuevo' = 'pendientes';

  constructor(private ticketsService: TicketsService) {}

  ngOnInit(): void {
    this.cargarPendientes();
  }

  cambiarVista(vista: 'pendientes' | 'atendidos' | 'nuevo') {
    this.vistaActual = vista;
  }

  cargarPendientes() {
    this.ticketsService.getTicketsPendientes().subscribe(data => {
      this.ticketsPendientes = data;
      this.vistaActual = 'pendientes';
    });
  }


  cargarAtendidos() {
    this.ticketsService.getTicketsAtendidos().subscribe(data => {
      this.ticketsAtendidos = data;
      this.vistaActual = 'atendidos';
    });
  }
  guardarTicket() {
    if (!this.nuevoTicket.descripcion.trim()) {
      alert("La descripción no puede estar vacía");
      return;
    }

    this.ticketsService.guardar(this.nuevoTicket).subscribe({
      next: (resp) => {
        this.ticketsPendientes.push(resp);

        this.nuevoTicket.descripcion = "";

        this.vistaActual = 'pendientes';

        alert("Ticket registrado correctamente");
      },
      error: () => alert("Error al registrar ticket")
    });
  }
  }
