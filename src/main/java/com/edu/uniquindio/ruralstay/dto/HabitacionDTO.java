package com.edu.uniquindio.ruralstay.dto;

import com.edu.uniquindio.ruralstay.entity.enums.TipoCama;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private Long codigo;
    private Integer numeroCamas;
    private TipoCama tipoCama;
    private Boolean tieneBano;
}
