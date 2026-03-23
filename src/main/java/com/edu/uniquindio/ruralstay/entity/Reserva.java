package com.edu.uniquindio.ruralstay.entity;

import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroReserva;

    @Column(nullable = false)
    private LocalDate fechaReserva;

    @Column(nullable = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false)
    private Integer numeroNoches;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadAlquiler modalidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importeTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importeAnticipo;

    @Column(nullable = false)
    private LocalDate fechaLimitePago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "casa_rural_id")
    private CasaRural casaRural;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Pago pago;
}
