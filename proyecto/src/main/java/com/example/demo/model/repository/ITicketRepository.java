package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entidad.Ticket;

public interface ITicketRepository extends JpaRepository<Ticket, Long>{
    
    List<Ticket> findByEstado(Ticket.EstadoTicket estado);

}
