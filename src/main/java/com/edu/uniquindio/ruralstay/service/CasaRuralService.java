package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.CasaRuralDTO;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CasaRuralService {

    private final CasaRuralRepository casaRuralRepository;

    public CasaRuralService(CasaRuralRepository casaRuralRepository) {
        this.casaRuralRepository = casaRuralRepository;
    }

    public List<CasaRural> listarTodas() {
        return casaRuralRepository.findAll();
    }

    public Optional<CasaRural> buscarPorId(Long id) {
        return casaRuralRepository.findById(id);
    }

    public CasaRural guardar(CasaRural casaRural) {
        return casaRuralRepository.save(casaRural);
    }

    public void eliminar(Long id) {
        casaRuralRepository.deleteById(id);
    }

    public List<CasaRuralDTO> listarPorPropietario(Long propietarioId) {
        return casaRuralRepository.findByPropietarioId(propietarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Metodo para filtrar casas disponibles por fecha de entrada y salida
    public CasaRuralDTO[] filtrarCasasDisponiblesRango(LocalDate fechaEntrada, LocalDate fechaSalida) {
        // Implementar lógica para filtrar casas disponibles según las fechas
        // Esto puede incluir verificar las reservas existentes para cada casa

        List<CasaRural> disponibles = casaRuralRepository.findDisponiblesByRango(fechaEntrada, fechaSalida);

        List<CasaRuralDTO> dtoList = disponibles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        System.out.println(disponibles);
        return dtoList.toArray(new CasaRuralDTO[0]); // Retornar un arreglo de DTOs de casas disponibles
    }

    private CasaRuralDTO toDTO(CasaRural casa) {
        return new CasaRuralDTO(
                casa.getCodigo(),
                casa.getPoblacion(),
                casa.getDescripcionGeneral(),
                casa.getNumeroDormitorios(),
                casa.getNumeroBanos(),
                casa.getNumeroCocinas(),
                casa.getNumeroComedores(),
                casa.getPlazasGaraje(),
                casa.getActiva(),
                casa.getPropietario().getId()
        );
    }
}
