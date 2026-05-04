package com.edu.uniquindio.ruralstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropietarioSimpleDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String telefono;
}
