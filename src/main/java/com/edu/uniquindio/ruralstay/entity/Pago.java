package com.edu.uniquindio.ruralstay.entity;

import com.edu.uniquindio.ruralstay.entity.enums.TipoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private String concepto;

    @Column(nullable = false)
    private Boolean verificado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipo;

    @OneToOne(optional = false)
    @JoinColumn(name = "reserva_id", unique = true)
    private Reserva reserva;
}
