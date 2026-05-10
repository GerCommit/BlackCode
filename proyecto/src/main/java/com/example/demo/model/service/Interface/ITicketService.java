package com.example.demo.model.service.Interface;

import java.util.List;
import com.example.demo.model.entidad.Ticket;

public interface ITicketService {
    List<Ticket> listar();
    Ticket obtenerPorId(Long id);
    void eliminar(Long id);
    List<Ticket> listarPendientes();
    List<Ticket> listarAtendidos();
    Ticket guardar(Ticket ticket);
    Ticket resolver(Long id);
}
