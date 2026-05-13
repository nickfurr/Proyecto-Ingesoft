package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.*;
import com.edu.uniquindio.ruralstay.entity.*;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                casa.getFotos(),
                casa.getCiudad(),
                casa.getPrecio()
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

        // Mapear fotos
        if (casa.getFotos() != null) {
            dto.setFotos(casa.getFotos().stream()
                    .filter(foto -> foto != null && !foto.isBlank())
                    .distinct()
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public CasaRuralDTO crearCasa(CrearCasaRuralDTO dto) {
        // Validaciones necesarias
        if (dto.getCocinas() == null || dto.getCocinas().isEmpty()) {
            throw new RuntimeException("Debe tener al menos 1 cocina");
        }

        if (dto.getHabitaciones() == null || dto.getHabitaciones().size() < 3) {
            throw new RuntimeException("Debe tener al menos 3 dormitorios");
        }

        if (dto.getBanos() == null || dto.getBanos().size() < 2) {
            throw new RuntimeException("Debe tener al menos 2 baños");
        }

        Propietario propietario = propietarioRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Propietario no existe con ID: " + dto.getPropietarioId()
                ));

        if (!propietario.getActivo()) {
            throw new RuntimeException("El propietario no está activo");
        }

        // Creación de la casa rural
        CasaRural casa = new CasaRural();
        casa.setPoblacion(dto.getPoblacion());
        // agregados
        casa.setCiudad(dto.getCiudad());
        casa.setPrecio(dto.getPrecio());

        casa.setDescripcionGeneral(dto.getDescripcionGeneral());
        casa.setNumeroDormitorios(dto.getHabitaciones().size());
        casa.setNumeroBanos(dto.getBanos().size());
        casa.setNumeroCocinas(dto.getCocinas().size());
        casa.setNumeroComedores(dto.getNumeroComedores());
        casa.setPlazasGaraje(dto.getPlazasGaraje());
        casa.setFotos(dto.getFotos());
        casa.setActiva(true);

        // Asociar el propietario a la casa rural creada
        casa.setPropietario(propietario);

        // =========================
        // HABITACIONES
        // =========================

        Set<Habitacion> habitaciones = dto.getHabitaciones()
                .stream()
                .map(hDto -> {

                    Habitacion habitacion = new Habitacion();

                    habitacion.setNumeroCamas(hDto.getNumeroCamas());
                    habitacion.setTipoCama(hDto.getTipoCama());
                    habitacion.setTieneBano(hDto.getTieneBano());

                    habitacion.setCasaRural(casa);

                    return habitacion;

                }).collect(Collectors.toSet());

        casa.setHabitaciones(habitaciones);

        // =========================
        // BAÑOS
        // =========================

        Set<Baño> banos = dto.getBanos()
                .stream()
                .map(bDto -> {

                    Baño bano = new Baño();

                    bano.setCompartido(bDto.getCompartido());

                    bano.setCasaRural(casa);

                    return bano;

                }).collect(Collectors.toSet());

        casa.setBanos(banos);

        // =========================
        // COCINAS
        // =========================

        Set<Cocina> cocinas = dto.getCocinas()
                .stream()
                .map(cDto -> {

                    Cocina cocina = new Cocina();

                    cocina.setTieneLavadora(cDto.getTieneLavadora());
                    cocina.setTieneLavavajillas(cDto.getTieneLavavajillas());

                    cocina.setCasaRural(casa);

                    return cocina;

                }).collect(Collectors.toSet());

        casa.setCocinas(cocinas);

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

    public List<CasaRuralDTO> filtrarCasas(FiltroDTO filtro) {

        if (
                filtro.getPrecioMin() != null &&
                        filtro.getPrecioMax() != null &&
                        filtro.getPrecioMin() > filtro.getPrecioMax()
        ) {
            throw new RuntimeException(
                    "El precio mínimo no puede ser mayor al máximo"
            );
        }

        List<CasaRural> casas = casaRuralRepository.filtrarCasas(
                filtro.getCiudad(),
                filtro.getPrecioMin(),
                filtro.getPrecioMax()
        );

        return casas.stream()
                .map(this::toDTO)
                .toList();
    }
}
