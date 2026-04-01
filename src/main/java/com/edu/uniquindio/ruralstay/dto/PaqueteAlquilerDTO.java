package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
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
public class PaqueteAlquilerDTO {
    public Long casaRuralId;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public ModalidadAlquiler modalidad;
    public BigDecimal precioCasaEntera;
    public BigDecimal precioPorHabitacion;
    public Boolean vigente;
}
