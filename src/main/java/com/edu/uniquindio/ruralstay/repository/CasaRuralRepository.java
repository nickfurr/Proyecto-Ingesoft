package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.entity.CasaRural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CasaRuralRepository extends JpaRepository<CasaRural, Long> {
    List<CasaRural> findByPropietarioId(Long propietarioId);

    @Query(value = """
            SELECT DISTINCT cr.* FROM casa_rural cr
            WHERE cr.activa = true
            AND NOT EXISTS (
                SELECT 1 FROM reserva r
                WHERE r.casa_rural_id = cr.codigo
                AND r.estado IN ('CONFIRMADA', 'PAGADA_PARCIAL', 'PENDIENTE_PAGO')
                AND r.fecha_entrada < :fechaSalida
                AND DATE_ADD(r.fecha_entrada, INTERVAL r.numero_noches DAY) > :fechaEntrada
            )
            """, nativeQuery = true)
    List<CasaRural> findDisponiblesByRango(
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida);

    @Query("""
            SELECT DISTINCT c
            FROM CasaRural c
            LEFT JOIN FETCH c.propietario
            LEFT JOIN FETCH c.cocinas
            LEFT JOIN FETCH c.habitaciones
            LEFT JOIN FETCH c.banos
            WHERE c.codigo = :id
            """)
    Optional<CasaRural> findCasaCompleta(@Param("id") Long id);
}
