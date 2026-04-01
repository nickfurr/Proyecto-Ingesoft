package com.edu.uniquindio.ruralstay.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasaRuralDTO {
    private Long codigo;
    private String poblacion;
    private String descripcionGeneral;
    private Integer numeroDormitorios;
    private Integer numeroBanos;
    private Integer numeroCocinas;
    private Integer numeroComedores;
    private Integer plazasGaraje;
    private Boolean activa;
    private Long propietarioId;
}
