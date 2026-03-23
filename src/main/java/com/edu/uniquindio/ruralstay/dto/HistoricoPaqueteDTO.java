package com.edu.uniquindio.ruralstay.dto;

import java.math.BigDecimal;
import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricoPaqueteDTO {

    private Long idPaquete;
    private BigDecimal precioCasaEntera;
    private ModalidadAlquiler modalidad;
    private Long totalReservas;
    private BigDecimal ingresos;

    public HistoricoPaqueteDTO(Long idPaquete, BigDecimal precioCasaEntera, ModalidadAlquiler modalidad,
                               Long totalReservas, BigDecimal ingresos) {
        this.idPaquete = idPaquete;
        this.precioCasaEntera = precioCasaEntera;
        this.modalidad = modalidad;
        this.totalReservas = totalReservas;
        this.ingresos = ingresos != null ? ingresos : BigDecimal.ZERO;
    }
}