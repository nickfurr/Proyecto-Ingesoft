package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.repository.PaqueteAlquilerRepository;
import org.springframework.stereotype.Service;

import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaqueteAlquilerService {

    private final PaqueteAlquilerRepository paqueteAlquilerRepository;
    private final PropietarioRepository propietarioRepository;

    public PaqueteAlquilerService(PaqueteAlquilerRepository paqueteAlquilerRepository, PropietarioRepository propietarioRepository) {
        this.paqueteAlquilerRepository = paqueteAlquilerRepository;
        this.propietarioRepository = propietarioRepository;
    }

    public List<PaqueteAlquiler> listarTodos() {
        return paqueteAlquilerRepository.findAll();
    }

    public Optional<PaqueteAlquiler> buscarPorId(Long id) {
        return paqueteAlquilerRepository.findById(id);
    }

    public PaqueteAlquiler guardar(PaqueteAlquiler paqueteAlquiler) {
        return paqueteAlquilerRepository.save(paqueteAlquiler);
    }

    public void eliminar(Long id) {
        paqueteAlquilerRepository.deleteById(id);
    }

    public List<HistoricoPaqueteDTO> obtenerHistorico(
            Long propietarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {

        if (propietarioId == null) {
            throw new RuntimeException("Propietario inválido");
        }

        if (!propietarioRepository.existsById(propietarioId)) {
            return List.of();
        }

        return paqueteAlquilerRepository.obtenerHistorico(
                propietarioId, fechaInicio, fechaFin
        );
    }
}
