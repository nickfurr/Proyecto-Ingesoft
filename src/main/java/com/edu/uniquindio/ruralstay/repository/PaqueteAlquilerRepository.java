package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaqueteAlquilerRepository extends JpaRepository<PaqueteAlquiler, Long> {
    @Query("""
                    SELECT new com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO(
                    p.id,
                    p.precioCasaEntera,
                    p.modalidad,
                    COUNT(r),
                    SUM(r.importeTotal)
                        )
                    FROM PaqueteAlquiler p
            LEFT JOIN Reserva r
                ON r.casaRural = p.casaRural
                AND r.fechaEntrada BETWEEN p.fechaInicio AND p.fechaFin
            WHERE p.casaRural.propietario.id = :propietarioId
            AND p.fechaInicio BETWEEN :fechaInicio AND :fechaFin
            GROUP BY p.id, p.precioCasaEntera, p.modalidad
            """)
    List<HistoricoPaqueteDTO> obtenerHistorico(
            @Param("propietarioId") Long propietarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    List<PaqueteAlquiler> findByCasaRural_Propietario_IdAndVigenteTrue(Long propietarioId);
}