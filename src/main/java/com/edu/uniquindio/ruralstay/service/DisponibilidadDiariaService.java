package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.DisponibilidadDiaria;
import com.edu.uniquindio.ruralstay.repository.DisponibilidadDiariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadDiariaService {

    private final DisponibilidadDiariaRepository disponibilidadDiariaRepository;

    public DisponibilidadDiariaService(DisponibilidadDiariaRepository disponibilidadDiariaRepository) {
        this.disponibilidadDiariaRepository = disponibilidadDiariaRepository;
    }

    public List<DisponibilidadDiaria> listarTodas() {
        return disponibilidadDiariaRepository.findAll();
    }

    public Optional<DisponibilidadDiaria> buscarPorId(Long id) {
        return disponibilidadDiariaRepository.findById(id);
    }

    public DisponibilidadDiaria guardar(DisponibilidadDiaria disponibilidad) {
        return disponibilidadDiariaRepository.save(disponibilidad);
    }

    public void eliminar(Long id) {
        disponibilidadDiariaRepository.deleteById(id);
    }
}
