package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.PropietarioDTO;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.service.PropietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/propietarios")
@CrossOrigin(origins = "http://localhost:4200")
public class PropietarioController {

    @Autowired
    private PropietarioService propietarioService;

    @GetMapping
    public List<Propietario> listarTodosPropietarios() {
        return propietarioService.listarTodos();
    }

    @PostMapping("/login-propietario")
    public ResponseEntity<PropietarioDTO> login(@RequestBody PropietarioDTO solicitud) {
        System.out.println("Iniciando login propietario");
        PropietarioDTO respuesta = propietarioService.login(solicitud);

        System.out.println("Descripcion respuesta: " + respuesta.getDescription());
        System.out.println("Respuesta completa: " + respuesta.getNumeroCuentaBancaria());

        if (respuesta.getDescription() == "Login exitoso") {
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<PropietarioDTO> buscarPorId(@PathVariable("id") Long id) {
        System.out.println("buscando el propietario com id: " + id);
        return ResponseEntity.ok(propietarioService.buscrPorId(id));
    }
}
