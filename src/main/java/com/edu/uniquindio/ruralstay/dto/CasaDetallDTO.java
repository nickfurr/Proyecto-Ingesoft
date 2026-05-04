package com.edu.uniquindio.ruralstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasaDetallDTO {
    private Long codigo;
    private Integer plazasParqueo;
    private String descripcion;
    private PropietarioSimpleDTO propietario;
    private List<CocinaDTO> cocinas;
    private List<HabitacionDTO> habitaciones;
    private List<BañoDTO> banos;
}
