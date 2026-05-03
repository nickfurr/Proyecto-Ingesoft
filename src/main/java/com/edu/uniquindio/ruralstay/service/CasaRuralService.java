package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.CasaRuralDTO;
import com.edu.uniquindio.ruralstay.dto.CrearCasaRuralDTO;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CasaRuralService {

    private final CasaRuralRepository casaRuralRepository;
    private final PropietarioRepository propietarioRepository;

    public CasaRuralService(CasaRuralRepository casaRuralRepository, PropietarioRepository propietarioRepository) {
        this.casaRuralRepository = casaRuralRepository;
        this.propietarioRepository = propietarioRepository;
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
                .filter(CasaRural::getActiva)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CasaRuralDTO> listarTodasDTO() {
        return casaRuralRepository.findAll()
                .stream()
                .filter(CasaRural::getActiva)
                .map(this::toDTO)
                .toList();
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
                casa.getPropietario().getId(),
                casa.getFotos()
        );
    }

    public CasaRuralDTO crearCasa(CrearCasaRuralDTO dto) {
        // Validaciones necesarias
        if (dto.getNumeroCocinas() == null || dto.getNumeroCocinas() < 1) {
            throw new RuntimeException("Debe tener al menos 1 cocina");
        }

        if (dto.getNumeroDormitorios() == null ||dto.getNumeroDormitorios() < 3) {
            throw new RuntimeException("Debe tener al menos 3 dormitorios");
        }

        if (dto.getNumeroBanos() == null ||dto.getNumeroBanos() < 2) {
            throw new RuntimeException("Debe tener al menos 2 baños");
        }

        Propietario propietario = propietarioRepository.findById(dto.getPropietarioId()) .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        if (!propietario.getActivo()) {
            throw new RuntimeException("El propietario no está activo");
        }

        // Creación de la casa rural
        CasaRural casa = new CasaRural();
        casa.setPoblacion(dto.getPoblacion());
        casa.setDescripcionGeneral(dto.getDescripcionGeneral());
        casa.setNumeroDormitorios(dto.getNumeroDormitorios());
        casa.setNumeroBanos(dto.getNumeroBanos());
        casa.setNumeroCocinas(dto.getNumeroCocinas());
        casa.setNumeroComedores(dto.getNumeroComedores());
        casa.setPlazasGaraje(dto.getPlazasGaraje());
        casa.setFotos(dto.getFotos());
        casa.setActiva(true);

        // Asociar el propietario a la casa rural creada
        casa.setPropietario(propietario);

        CasaRural casaGuardada = casaRuralRepository.save(casa);

        return toDTO(casaGuardada);
    }

    public void eliminarCasa(Long casaId, Long propietarioId) {
        CasaRural casa = propietarioRepository
                .buscarCasaDePropietario(casaId, propietarioId)
                .orElseThrow(() -> new RuntimeException("No puedes eliminar esta casa"));

        casa.setActiva(false); // Simplemente se desactiva
        casaRuralRepository.save(casa);
    }
}
