package com.edu.uniquindio.ruralstay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cocina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean tieneLavavajillas;

    @Column(nullable = false)
    private Boolean tieneLavadora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "casa_rural_id")
    private CasaRural casaRural;
}
