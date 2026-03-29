package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private Long numeroReserva;
    private Long casaRural;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String telefonoCliente;
    private BigDecimal importeTotal;
    private EstadoReserva estadoPago;
}
