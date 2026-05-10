package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entidad.Ticket;
import com.example.demo.model.entidad.Ticket.EstadoTicket;
import com.example.demo.model.service.Interface.ITicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

 private final ITicketService service;

    @GetMapping("/")
    public ResponseEntity<List<Ticket>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Ticket>> pendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @GetMapping("/atendidos")
    public ResponseEntity<List<Ticket>> atendidos() {
        return ResponseEntity.ok(service.listarAtendidos());
    }

    @PostMapping("/guardar")
    public ResponseEntity<Ticket> guardar(@RequestBody Ticket ticket) {
        ticket.setEstado(Ticket.EstadoTicket.pendiente);
        ticket.setFechaRegistro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(ticket));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/resolver/{id}")
    public ResponseEntity<Ticket> resolverTicket(
            @PathVariable Long id,
            @RequestBody Ticket data) {

        Ticket ticket = service.obtenerPorId(id);

        if (ticket == null) return ResponseEntity.notFound().build();

        ticket.setSolucion(data.getSolucion());
        ticket.setEstado(EstadoTicket.resuelto);

        return ResponseEntity.ok(service.guardar(ticket));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Ticket> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }


}
