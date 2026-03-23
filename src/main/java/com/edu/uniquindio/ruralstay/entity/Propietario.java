package com.edu.uniquindio.ruralstay.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Propietario extends Usuario {

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String numeroCuentaBancaria;

    @Column(nullable = false)
    private Boolean activo;
}
