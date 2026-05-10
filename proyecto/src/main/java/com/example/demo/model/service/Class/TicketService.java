package com.example.demo.model.service.Class;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entidad.Ticket;
import com.example.demo.model.repository.ITicketRepository;
import com.example.demo.model.service.Interface.ITicketService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService{

    private final ITicketRepository repository;

    @Override
    public List<Ticket> listar() {
        return repository.findAll();
    }

    @Override
    public Ticket obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Ticket> listarPendientes() {
        return repository.findByEstado(Ticket.EstadoTicket.pendiente);
    }

    @Override
    public List<Ticket> listarAtendidos() {
        return repository.findByEstado(Ticket.EstadoTicket.resuelto);
    }

    @Override
    public Ticket guardar(Ticket ticket) {
        return repository.save(ticket);
    }

    @Override
    public Ticket resolver(Long id) {

        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        ticket.setEstado(Ticket.EstadoTicket.resuelto);

        return repository.save(ticket);
    }
}
