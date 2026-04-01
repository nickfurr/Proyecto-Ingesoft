package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.dto.PaqueteAlquilerDTO;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
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
    private final CasaRuralRepository casaRuralRepository;

    public PaqueteAlquilerService(PaqueteAlquilerRepository paqueteAlquilerRepository,
                                  PropietarioRepository propietarioRepository, CasaRuralRepository casaRuralRepository) {
        this.paqueteAlquilerRepository = paqueteAlquilerRepository;
        this.propietarioRepository = propietarioRepository;
        this.casaRuralRepository = casaRuralRepository;
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

    public PaqueteAlquilerDTO crearPaquete(PaqueteAlquilerDTO dto, Long propietarioId) {

        CasaRural casa = propietarioRepository.buscarCasaDePropietario(dto.getCasaRuralId(), propietarioId)
                .orElseThrow(() -> new RuntimeException("La casa no pertenece al propietario"));

        PaqueteAlquiler paquete = new PaqueteAlquiler();
        paquete.setCasaRural(casa);
        paquete.setFechaInicio(dto.getFechaInicio());
        paquete.setFechaFin(dto.getFechaFin());
        paquete.setModalidad(dto.getModalidad());
        paquete.setPrecioCasaEntera(dto.getPrecioCasaEntera());
        paquete.setPrecioPorHabitacion(dto.getPrecioPorHabitacion());
        paquete.setVigente(true);

        PaqueteAlquiler guardado = paqueteAlquilerRepository.save(paquete);

        return toDTO(guardado);
    }

    public PaqueteAlquilerDTO modificarPaquete(Long id, PaqueteAlquilerDTO dto, Long propietarioId) {
        PaqueteAlquiler paquete = paqueteAlquilerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        if (!paquete.getCasaRural().getPropietario().getId().equals(propietarioId)) {
            throw new RuntimeException("No tienes permiso para modificar este paquete");
        }

        CasaRural casa = casaRuralRepository.findById(dto.getCasaRuralId())
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        if (!casa.getPropietario().getId().equals(propietarioId)) {
            throw new RuntimeException("La casa no pertenece al propietario");
        }

        paquete.setCasaRural(casa);
        paquete.setFechaInicio(dto.getFechaInicio());
        paquete.setFechaFin(dto.getFechaFin());
        paquete.setModalidad(dto.getModalidad());
        paquete.setPrecioCasaEntera(dto.getPrecioCasaEntera());
        paquete.setPrecioPorHabitacion(dto.getPrecioPorHabitacion());

        PaqueteAlquiler actualizado = paqueteAlquilerRepository.save(paquete);

        return toDTO(actualizado);
    }

    public List<PaqueteAlquilerDTO> listarActivosPorPropietario(Long propietarioId) {
        return paqueteAlquilerRepository.findByCasaRural_Propietario_IdAndVigenteTrue(propietarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PaqueteAlquilerDTO toDTO(PaqueteAlquiler paquete) {
        PaqueteAlquilerDTO dto = new PaqueteAlquilerDTO();
        dto.setCasaRuralId(paquete.getCasaRural().getCodigo());
        dto.setFechaInicio(paquete.getFechaInicio());
        dto.setFechaFin(paquete.getFechaFin());
        dto.setModalidad(paquete.getModalidad());
        dto.setPrecioCasaEntera(paquete.getPrecioCasaEntera());
        dto.setPrecioPorHabitacion(paquete.getPrecioPorHabitacion());
        dto.setVigente(paquete.getVigente());
        return dto;
    }
}
