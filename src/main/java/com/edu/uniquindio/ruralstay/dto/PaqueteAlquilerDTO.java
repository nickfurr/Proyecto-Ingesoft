package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaqueteAlquilerDTO {
    public Long casaRuralId;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public ModalidadAlquiler modalidad;
    public BigDecimal precioCasaEntera;
    public BigDecimal precioPorHabitacion;
}
