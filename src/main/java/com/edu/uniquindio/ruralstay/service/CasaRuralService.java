package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.*;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Optional<CasaDetallDTO> buscarPorId(Long id) {
        Optional<CasaRural> casa = casaRuralRepository.findCasaCompleta(id);
        return casa.map(this::toCasaDetallDTO);
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

    private CasaDetallDTO toCasaDetallDTO(CasaRural casa) {
        CasaDetallDTO dto = new CasaDetallDTO();
        dto.setCodigo(casa.getCodigo());
        dto.setPlazasParqueo(casa.getPlazasGaraje());
        dto.setDescripcion(casa.getDescripcionGeneral());
        
        // Mapear propietario
        Propietario propietario = casa.getPropietario();
        PropietarioSimpleDTO propietarioDTO = new PropietarioSimpleDTO(
                propietario.getId(),
                propietario.getNombreCompleto(),
                propietario.getEmail(),
                propietario.getTelefono()
        );
        dto.setPropietario(propietarioDTO);
        
        // Mapear cocinas
        if (casa.getCocinas() != null) {
            dto.setCocinas(casa.getCocinas().stream()
                    .map(cocina -> new CocinaDTO(
                            cocina.getId(),
                            cocina.getTieneLavavajillas(),
                            cocina.getTieneLavadora()
                    ))
                    .collect(Collectors.toList()));
        }
        
        // Mapear habitaciones
        if (casa.getHabitaciones() != null) {
            dto.setHabitaciones(casa.getHabitaciones().stream()
                    .map(habitacion -> new HabitacionDTO(
                            habitacion.getCodigo(),
                            habitacion.getNumeroCamas(),
                            habitacion.getTipoCama(),
                            habitacion.getTieneBano()
                    ))
                    .collect(Collectors.toList()));
        }
        
        // Mapear baños
        if (casa.getBanos() != null) {
            dto.setBanos(casa.getBanos().stream()
                    .map(bano -> new BañoDTO(
                            bano.getId(),
                            bano.getCompartido()
                    ))
                    .collect(Collectors.toList()));
        }
        
        return dto;
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
