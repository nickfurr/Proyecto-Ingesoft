package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.CrearReservaDTO;
import com.edu.uniquindio.ruralstay.dto.ClienteDTO;
import com.edu.uniquindio.ruralstay.dto.ReservaDTO;
import com.edu.uniquindio.ruralstay.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/login-cliente")
    public ResponseEntity<ClienteDTO> login(@RequestBody ClienteDTO solicitud) {
        ClienteDTO respuesta = clienteService.login(solicitud);

        if ("Login exitoso".equals(respuesta.getDescription())) {
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @PostMapping("/reservas")
    public ResponseEntity<ReservaDTO> registrarReserva(@RequestBody CrearReservaDTO solicitud) {
        ReservaDTO respuesta = clienteService.registrarReserva(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
