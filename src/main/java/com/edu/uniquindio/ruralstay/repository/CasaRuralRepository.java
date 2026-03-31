package com.edu.uniquindio.ruralstay.repository;

import com.edu.uniquindio.ruralstay.entity.CasaRural;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasaRuralRepository extends JpaRepository<CasaRural, Long> {
    List<CasaRural> findByPropietarioId(Long propietarioId);
}
