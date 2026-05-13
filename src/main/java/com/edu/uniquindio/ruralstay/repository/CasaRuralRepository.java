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
            LEFT JOIN FETCH c.fotos
            WHERE c.codigo = :id
            """)
    Optional<CasaRural> findCasaCompleta(@Param("id") Long id);


    // decirle a carlos que en la historia de usuario 13 le falto el precio y la ciudad
// las cuales se agregaron nuevas para las historias de usuario 10 y 11

    @Query("""
        SELECT c FROM CasaRural c
        WHERE (:min IS NULL OR c.precio >= :min)
        AND (:max IS NULL OR c.precio <= :max)
    """)
    List<CasaRural> filtrarPorPrecio(
            @Param("min") Double min,
            @Param("max") Double max
    );

    List<CasaRural> findByCiudadIgnoreCase(String ciudad);

    // Filtro completo
    @Query("""
        SELECT c FROM CasaRural c
        WHERE (:ciudad IS NULL OR LOWER(c.ciudad) = LOWER(:ciudad))
        AND (:min IS NULL OR c.precio >= :min)
        AND (:max IS NULL OR c.precio <= :max)
    """)
    List<CasaRural> filtrarCasas(
            @Param("ciudad") String ciudad,
            @Param("min") Double min,
            @Param("max") Double max
    );
}
