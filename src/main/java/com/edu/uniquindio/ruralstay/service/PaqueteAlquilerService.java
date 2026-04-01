package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.dto.PaqueteAlquilerDTO;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.repository.PaqueteAlquilerRepository;
import org.springframework.stereotype.Service;

import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaqueteAlquilerService {

    private final PaqueteAlquilerRepository paqueteAlquilerRepository;
    private final PropietarioRepository propietarioRepository;

    public PaqueteAlquilerService(PaqueteAlquilerRepository paqueteAlquilerRepository,
            PropietarioRepository propietarioRepository) {
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

    public void desactivarPaquete(Long paqueteId, Long propietarioId) {
        PaqueteAlquiler paquete = paqueteAlquilerRepository.findById(paqueteId)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        if (!paquete.getCasaRural().getPropietario().getId().equals(propietarioId)) {
            throw new RuntimeException("No tiene permisos para eliminar este paquete");
        }

        paquete.setVigente(false);
        paqueteAlquilerRepository.save(paquete);
    }

    public List<HistoricoPaqueteDTO> obtenerHistorico(
            Long propietarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        if (propietarioId == null) {
            throw new RuntimeException("Propietario inválido");
        }

        if (!propietarioRepository.existsById(propietarioId)) {
            return List.of();
        }

        return paqueteAlquilerRepository.obtenerHistorico(
                propietarioId, fechaInicio, fechaFin);
    }
    public PaqueteAlquiler crearPaquete(PaqueteAlquilerDTO dto, Long propietarioId) {

        CasaRural casa = propietarioRepository.buscarCasaDePropietario(dto.casaRuralId, propietarioId)
                .orElseThrow(() -> new RuntimeException("La casa no pertenece al propietario"));

        PaqueteAlquiler paquete = new PaqueteAlquiler();
        paquete.setCasaRural(casa);
        paquete.setFechaInicio(dto.fechaInicio);
        paquete.setFechaFin(dto.fechaFin);
        paquete.setModalidad(dto.modalidad);
        paquete.setPrecioCasaEntera(dto.precioCasaEntera);
        paquete.setPrecioPorHabitacion(dto.precioPorHabitacion);
        paquete.setVigente(true);

        return paqueteAlquilerRepository.save(paquete);
    }

    public PaqueteAlquiler modificarPaquete(Long paqueteId, PaqueteAlquilerDTO dto, Long propietarioId) {

        PaqueteAlquiler paqueteActual = paqueteAlquilerRepository.findById(paqueteId)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        if (!paqueteActual.getCasaRural().getPropietario().getId().equals(propietarioId)) {
            throw new RuntimeException("No tiene permisos para modificar este paquete");
        }

        paqueteActual.setVigente(false);
        paqueteAlquilerRepository.save(paqueteActual);

        PaqueteAlquiler nuevoPaquete = new PaqueteAlquiler();
        nuevoPaquete.setCasaRural(paqueteActual.getCasaRural());
        nuevoPaquete.setFechaInicio(dto.fechaInicio);
        nuevoPaquete.setFechaFin(dto.fechaFin);
        nuevoPaquete.setModalidad(dto.modalidad);
        nuevoPaquete.setPrecioCasaEntera(dto.precioCasaEntera);
        nuevoPaquete.setPrecioPorHabitacion(dto.precioPorHabitacion);
        nuevoPaquete.setVigente(true);

        return paqueteAlquilerRepository.save(nuevoPaquete);
    }

    public List<PaqueteAlquilerDTO> listarActivosPorPropietario(Long propietarioId) {
        return paqueteAlquilerRepository.findByCasaRural_Propietario_IdAndVigenteTrue(propietarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PaqueteAlquilerDTO toDTO(PaqueteAlquiler paquete) {
        PaqueteAlquilerDTO dto = new PaqueteAlquilerDTO();
        dto.casaRuralId = paquete.getCasaRural().getCodigo();
        dto.fechaInicio = paquete.getFechaInicio();
        dto.fechaFin = paquete.getFechaFin();
        dto.modalidad = paquete.getModalidad();
        dto.precioCasaEntera = paquete.getPrecioCasaEntera();
        dto.precioPorHabitacion = paquete.getPrecioPorHabitacion();
        return dto;
    }
}
