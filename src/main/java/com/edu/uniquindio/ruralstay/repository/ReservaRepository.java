package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.entity.Reserva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByPropietarioId(Long id);

    Object findByNumeroReserva(Long numeroReserva);
}
