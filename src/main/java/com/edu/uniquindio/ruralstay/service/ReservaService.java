package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.ReservaDTO;
import com.edu.uniquindio.ruralstay.entity.Pago;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }

    public List<ReservaDTO> listarPorPropietario(Long propietarioId) {
        return reservaRepository.findByPropietarioId(propietarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    

    //actuqalizar estado de la reserva
    public ReservaDTO actualizarReservaEstado(ReservaDTO reservaDTO) {
        // Validar que la reserva exista
        Reserva reserva = reservaRepository.findById(reservaDTO.getNumeroReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Actualizar campos relevantes
        reserva.setEstado(reservaDTO.getEstadoPago());

        // Guardar cambios
        Reserva reservaActualizada = reservaRepository.save(reserva);
        // devolver DTO actualizado
        return toDTO(reservaActualizada);
    }

    @Transactional
    public ReservaDTO registrarPago(Long numeroReserva) {
        Reserva reserva = reservaRepository.findById(numeroReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.PAGADA_PARCIAL) {
            throw new RuntimeException("Solo se puede registrar pago en reservas con estado PAGADA_PARCIAL");
        }

        Pago pago = reserva.getPago();
        if (pago != null) {
            pago.setVerificado(true);
        }

        reserva.setEstado(EstadoReserva.CONFIRMADA);

        Reserva reservaActualizada = reservaRepository.save(reserva);
        return toDTO(reservaActualizada);
    }

    private ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(
                reserva.getNumeroReserva(),
                reserva.getCasaRural().getCodigo(),
                reserva.getFechaEntrada(),
                reserva.getFechaEntrada().plusDays(reserva.getNumeroNoches()),
                reserva.getCliente() != null ? reserva.getCliente().getTelefonoContacto() : null,
                reserva.getImporteTotal(),
                reserva.getEstado()
        );
    }
}