package com.edu.uniquindio.ruralstay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente extends Usuario {

    @Column(nullable = false)
    private String telefonoContacto;
}
