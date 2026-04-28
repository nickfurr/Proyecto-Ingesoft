package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    @Query("""
           SELECT c FROM CasaRural c
           WHERE c.codigo = :casaId
           AND c.propietario.id = :propietarioId
           """)
    Optional<CasaRural> buscarCasaDePropietario(
            @Param("casaId") Long casaId,
            @Param("propietarioId") Long propietarioId);

    Optional<Propietario> findByEmail(String email);

    Optional<Propietario> findById(Long id);

    Optional<Propietario> findByUsername(String username);
}
