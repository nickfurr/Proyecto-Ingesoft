package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class HistoricoPaqueteDTO {

    private Long idPaquete;
    private Long casaRuralId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private ModalidadAlquiler modalidad;
    private BigDecimal precioCasaEntera;
    private BigDecimal precioPorHabitacion;
    private Boolean vigente;
    private Long totalReservas;
    private BigDecimal ingresos;

    public HistoricoPaqueteDTO(
            Long idPaquete,
            Long casaRuralId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            ModalidadAlquiler modalidad,
            BigDecimal precioCasaEntera,
            BigDecimal precioPorHabitacion,
            Boolean vigente,
            Long totalReservas,
            BigDecimal ingresos) {
        this.idPaquete = idPaquete;
        this.casaRuralId = casaRuralId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.modalidad = modalidad;
        this.precioCasaEntera = precioCasaEntera;
        this.precioPorHabitacion = precioPorHabitacion;
        this.vigente = vigente;
        this.totalReservas = totalReservas;
        this.ingresos = ingresos != null ? ingresos : BigDecimal.ZERO;
    }
}