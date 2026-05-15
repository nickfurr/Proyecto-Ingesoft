package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByPropietarioId(Long id);

    Optional<Reserva> findByNumeroReserva(Long numeroReserva);

    @Query(value = """
            SELECT r.* FROM reserva r
            WHERE r.casa_rural_id = :casaId
            AND r.estado IN ('CONFIRMADA', 'PAGADA_PARCIAL', 'PENDIENTE_PAGO')
            AND r.fecha_entrada < :fechaSalida
            AND DATE_ADD(r.fecha_entrada, INTERVAL r.numero_noches DAY) > :fechaEntrada
            """, nativeQuery = true)
    List<Reserva> findConflictosPorCasaYRango(
            @Param("casaId") Long casaId,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida);

}
