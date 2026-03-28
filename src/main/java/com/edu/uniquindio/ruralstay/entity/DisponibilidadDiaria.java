package com.edu.uniquindio.ruralstay.entity;

import com.edu.uniquindio.ruralstay.entity.enums.EstadoDisponibilidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fecha", "casa_rural_id"})
})
public class DisponibilidadDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDisponibilidad estadoCasa;

    private String observacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "casa_rural_id")
    private CasaRural casaRural;
}
