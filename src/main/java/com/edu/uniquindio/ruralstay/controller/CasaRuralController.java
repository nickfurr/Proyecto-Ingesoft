package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.dto.CasaDetallDTO;
import com.edu.uniquindio.ruralstay.dto.CasaRuralDTO;
import com.edu.uniquindio.ruralstay.dto.CrearCasaRuralDTO;
import com.edu.uniquindio.ruralstay.service.CasaRuralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/casas")
@CrossOrigin(origins = "http://localhost:4200")
public class CasaRuralController {

    private final CasaRuralService casaRuralService;

    public CasaRuralController(CasaRuralService casaRuralService) {
        this.casaRuralService = casaRuralService;
    }

    @GetMapping("/propietario/{propietarioId}")
    public List<CasaRuralDTO> listarPorPropietario(@PathVariable("propietarioId") Long propietarioId) {
        return casaRuralService.listarPorPropietario(propietarioId);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<CasaDetallDTO> obtenerDetalleCasa(@PathVariable("id") Long id) {
        System.out.println("entro y respondio" + casaRuralService.buscarPorId(id).toString());
        return casaRuralService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/disponibles")
    public CasaRuralDTO[] filtrarCasasDisponiblesRango(
            @RequestParam("fechaEntrada") String fechaEntrada,
            @RequestParam("fechaSalida") String fechaSalida) {
        return casaRuralService.filtrarCasasDisponiblesRango(
                java.time.LocalDate.parse(fechaEntrada),
                java.time.LocalDate.parse(fechaSalida)
        );
    }


    @GetMapping
    public List<CasaRuralDTO> listarTodas() {
        return casaRuralService.listarTodasDTO();
    }

    @PostMapping
    public CasaRuralDTO crearCasa (@RequestBody CrearCasaRuralDTO dto) {
        return casaRuralService.crearCasa(dto);
    }

    @DeleteMapping("/{casaId}/propietario/{propietarioId}")
    public void eliminarCasa(@PathVariable Long casaId, @PathVariable Long propietarioId) {
        casaRuralService.eliminarCasa(casaId, propietarioId);
    }
}
