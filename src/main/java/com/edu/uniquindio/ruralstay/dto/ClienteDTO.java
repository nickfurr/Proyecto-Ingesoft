package com.edu.uniquindio.ruralstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private String description;
    private long id;
    private String username;
    private String password;
    private String email;
    private String telefonoContacto;
}
