package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GestionReserva {

    @Autowired
    private ReservaRepository reservaRepository;

    // lista las reservas de tal propietario con tales casas pendientes de pago
    public List<Reserva> listarReservasPropietario(Propietario propietario) {
        return reservaRepository.findByPropietarioId(propietario.getId());
    }

    public void confirmarPagoInicialReserva(Long numeroReserva, BigDecimal montoAnticipo, Propietario propietario){

        Reserva reserva = reservaRepository.findByNumeroReserva(numeroReserva)
                .orElseThrow(() -> new java.util.NoSuchElementException("No se encontró la reserva"));

        if (!propietario.getId().equals(reserva.getPropietario().getId())) {
            throw new java.util.NoSuchElementException("El propietario no es el de la reserva");
        }
        
        reserva.setEstado(EstadoReserva.PAGADA_PARCIAL);
        reserva.setImporteAnticipo(montoAnticipo);
        reservaRepository.save(reserva);
    }


    public void confirmarPagoTotalReserva(Long numeroReserva, BigDecimal monto, Propietario propietario){

        Reserva reserva = reservaRepository.findByNumeroReserva(numeroReserva)
                .orElseThrow(() -> new java.util.NoSuchElementException("No se encontró la reserva"));

        if (!propietario.getId().equals(reserva.getPropietario().getId())) {
            throw new java.util.NoSuchElementException("El propietario no es el de la reserva");
        }
        
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reserva.setImporteTotal(reserva.getImporteAnticipo().add(monto));

        reservaRepository.save(reserva);
    }

}

