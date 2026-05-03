package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.ClienteDTO;
import com.edu.uniquindio.ruralstay.dto.PropietarioDTO;
import com.edu.uniquindio.ruralstay.entity.Cliente;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.ClienteRepository;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioSearchController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @GetMapping("/buscar/cliente")
    public ResponseEntity<?> buscarClientePorUsername(@RequestParam("username") String username) {
        Optional<Cliente> clienteOpt = clienteRepository.findByUsername(username);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new ClienteDTO("Cliente no encontrado", 0, null, null, null, null));
        }
        Cliente cliente = clienteOpt.get();
        return ResponseEntity.ok(new ClienteDTO(
                "Cliente encontrado",
                cliente.getId(),
                cliente.getUsername(),
            null,
                cliente.getEmail(),
                cliente.getTelefonoContacto()
        ));
    }

    @GetMapping("/buscar/propietario")
    public ResponseEntity<?> buscarPropietarioPorUsername(@RequestParam("username") String username) {
        Optional<Propietario> propietarioOpt = propietarioRepository.findByUsername(username);
        if (propietarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new PropietarioDTO("Propietario no encontrado", 0, null,
                            null, null, null, null, null, null));
        }
        Propietario propietario = propietarioOpt.get();
        return ResponseEntity.ok(new PropietarioDTO(
                "Propietario encontrado",
                propietario.getId(),
                propietario.getUsername(),
                propietario.getPassword(),
                propietario.getEmail(),
                propietario.getNombreCompleto(),
                propietario.getTelefono(),
                propietario.getNumeroCuentaBancaria(),
                propietario.getActivo()
        ));
    }
}
