package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CasaRuralService {

    private final CasaRuralRepository casaRuralRepository;

    public CasaRuralService(CasaRuralRepository casaRuralRepository) {
        this.casaRuralRepository = casaRuralRepository;
    }

    public List<CasaRural> listarTodas() {
        return casaRuralRepository.findAll();
    }

    public Optional<CasaRural> buscarPorId(Long id) {
        return casaRuralRepository.findById(id);
    }

    public CasaRural guardar(CasaRural casaRural) {
        return casaRuralRepository.save(casaRural);
    }

    public void eliminar(Long id) {
        casaRuralRepository.deleteById(id);
    }
}
