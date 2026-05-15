package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDTO {

    private Long clienteId;
    private Long casaRuralId;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private ModalidadAlquiler modalidad;
}