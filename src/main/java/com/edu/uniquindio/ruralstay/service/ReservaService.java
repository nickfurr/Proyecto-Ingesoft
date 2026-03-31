package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.ReservaDTO;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.repository.ReservaRepository;
import org.springframework.stereotype.Service;

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
