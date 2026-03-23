package com.edu.uniquindio.ruralstay.entity;

import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class PaqueteAlquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadAlquiler modalidad;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioCasaEntera;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioPorHabitacion;

    @Column(nullable = false)
    private Boolean vigente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "casa_rural_id")
    private CasaRural casaRural;
}
