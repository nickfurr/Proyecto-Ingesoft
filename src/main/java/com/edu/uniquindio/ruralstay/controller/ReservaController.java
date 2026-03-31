package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.ReservaDTO;
import com.edu.uniquindio.ruralstay.service.ReservaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/propietario/{propietarioId}")
    public List<ReservaDTO> listarPorPropietario(@PathVariable Long propietarioId) {
        return reservaService.listarPorPropietario(propietarioId);
    }
}
