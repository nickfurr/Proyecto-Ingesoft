package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.dto.PaqueteAlquilerDTO;
import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.service.PaqueteAlquilerService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/v1/paquetes")
@CrossOrigin(origins = "http://localhost:4200")
public class PaqueteAlquilerController {
    private final PaqueteAlquilerService service;

    public PaqueteAlquilerController(PaqueteAlquilerService service) {
        this.service = service;
    }

    @PostMapping
    public PaqueteAlquilerDTO crearPaquete(
            @RequestBody PaqueteAlquilerDTO dto,
            @RequestParam("propietarioId") Long propietarioId) {

        return service.crearPaquete(dto, propietarioId);
    }

    @PutMapping("/{id}")
    public PaqueteAlquilerDTO modificarPaquete(
            @PathVariable("id") Long id,
            @RequestBody PaqueteAlquilerDTO dto,
            @RequestParam("propietarioId") Long propietarioId) {

        return service.modificarPaquete(id, dto, propietarioId);
    }

    @GetMapping
    public List<PaqueteAlquiler> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PaqueteAlquiler obtener(@PathVariable("id") Long id) {
        return service.buscarPorId(id).orElseThrow();
    }

    @GetMapping("/propietario/{propietarioId}/activos")
    public List<PaqueteAlquilerDTO> listarActivosPorPropietario(@PathVariable("propietarioId") Long propietarioId) {
        return service.listarActivosPorPropietario(propietarioId);
    }

    @GetMapping("/propietario/{propietarioId}/historico")
    public List<HistoricoPaqueteDTO> obtenerHistorico(
            @PathVariable("propietarioId") Long propietarioId,
            @RequestParam("fechaInicio") LocalDate fechaInicio,
            @RequestParam("fechaFin") LocalDate fechaFin) {

        return service.obtenerHistorico(propietarioId, fechaInicio, fechaFin);
    }

    @PutMapping("/{id}/desactivar")
    public void desactivarPaquete(
            @PathVariable("id") Long id,
            @RequestParam("propietarioId") Long propietarioId) {

        service.desactivarPaquete(id, propietarioId);
    }
}
