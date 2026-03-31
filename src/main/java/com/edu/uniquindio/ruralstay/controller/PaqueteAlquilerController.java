package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.PaqueteAlquilerDTO;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.service.PaqueteAlquilerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class PaqueteAlquilerController {
    private final PaqueteAlquilerService service;

    public PaqueteAlquilerController(PaqueteAlquilerService service) {
        this.service = service;
    }

    @PostMapping
    public PaqueteAlquiler crearPaquete(
            @RequestBody PaqueteAlquilerDTO dto,
            @RequestParam Long propietarioId) {

        return service.crearPaquete(dto, propietarioId);
    }

    @PutMapping("/{id}")
    public PaqueteAlquiler modificarPaquete(
            @PathVariable Long id,
            @RequestBody PaqueteAlquilerDTO dto,
            @RequestParam Long propietarioId) {

        return service.modificarPaquete(id, dto, propietarioId);
    }

    @GetMapping
    public List<PaqueteAlquiler> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PaqueteAlquiler obtener(@PathVariable Long id) {
        return service.buscarPorId(id).orElseThrow();
    }
}
