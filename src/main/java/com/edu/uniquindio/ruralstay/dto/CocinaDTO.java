package com.edu.uniquindio.ruralstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CocinaDTO {
    private Long id;
    private Boolean tieneLavavajillas;
    private Boolean tieneLavadora;
}
