package com.edu.uniquindio.ruralstay.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class CasaRural {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false)
    private String poblacion;

    @Column(length = 1000)
    private String descripcionGeneral;

    @Column(nullable = false)
    private Integer numeroDormitorios;

    @Column(nullable = false)
    private Integer numeroBanos;

    @Column(nullable = false)
    private Integer numeroCocinas;

    @Column(nullable = false)
    private Integer numeroComedores;

    @Column(nullable = false)
    private Integer plazasGaraje;

    @Column(nullable = false)
    private Boolean activa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id")
    @JsonBackReference
    private Propietario propietario;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitacion> habitaciones;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cocina> cocinas;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Baño> banos;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaqueteAlquiler> paquetesAlquiler;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisponibilidadDiaria> disponibilidades;

    @OneToMany(mappedBy = "casaRural")
    private List<Reserva> reservas;

    @ElementCollection
    @CollectionTable(name = "casaFotos", joinColumns = @JoinColumn(name = "casaId"))
    @Column(name = "fotoUrl")
    private List<String> fotos;
}
