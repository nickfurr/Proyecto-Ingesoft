package com.edu.uniquindio.ruralstay.entity;

import com.edu.uniquindio.ruralstay.entity.enums.TipoCama;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false)
    private Integer numeroCamas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCama tipoCama;

    @Column(nullable = false)
    private Boolean tieneBano;

    @ManyToOne(optional = false)
    @JoinColumn(name = "casa_rural_id")
    private CasaRural casaRural;
}
