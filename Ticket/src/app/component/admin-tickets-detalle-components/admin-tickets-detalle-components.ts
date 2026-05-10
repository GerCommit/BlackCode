import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Ticket } from '../../model/ticket';
import { AdminTicketsService } from '../../service/admin-tickets-service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-tickets-detalle-components',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-tickets-detalle-components.html',
  styleUrl: './admin-tickets-detalle-components.css',
})
export class AdminTicketsDetalleComponents implements OnInit{
  ticket?: Ticket;
  solucion: string = '';

  constructor(
    private route: ActivatedRoute,
    private service: AdminTicketsService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.service.listarTodos().subscribe(data => {
      this.ticket = data.find(t => t.idTicket === id);
    });
  }

  confirmarSolucion() {
    if (!this.ticket) return;

    this.service.resolver(this.ticket!.idTicket!, this.solucion).subscribe(() => {
      alert('Ticket resuelto');
      window.location.href = '/admin/tickets';
    });
  }
  volver() {
  window.location.href = '/admin/tickets';
  }
}
