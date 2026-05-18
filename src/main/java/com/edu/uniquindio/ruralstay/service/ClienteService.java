package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.CrearReservaDTO;
import com.edu.uniquindio.ruralstay.dto.ClienteDTO;
import com.edu.uniquindio.ruralstay.dto.ReservaDTO;
import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.Cliente;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import com.edu.uniquindio.ruralstay.repository.ClienteRepository;
import com.edu.uniquindio.ruralstay.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ClienteService {

    private static final Logger LOGGER = Logger.getLogger(ClienteService.class.getName());

    private final ClienteRepository clienteRepository;
    private final CasaRuralRepository casaRuralRepository;
    private final ReservaRepository reservaRepository;
    private final EmailService emailService;

    public ClienteService(ClienteRepository clienteRepository,
                          CasaRuralRepository casaRuralRepository,
                          ReservaRepository reservaRepository,
                          EmailService emailService) {
        this.clienteRepository = clienteRepository;
        this.casaRuralRepository = casaRuralRepository;
        this.reservaRepository = reservaRepository;
        this.emailService = emailService;
    }

    public ClienteDTO login(ClienteDTO solicitud) {
        String identificador = solicitud.getEmail();
        if (identificador == null || identificador.isBlank()) {
            identificador = solicitud.getUsername();
        }

        Optional<Cliente> clienteOpt = buscarPorIdentificador(identificador);

        if (clienteOpt.isEmpty()) {
            return new ClienteDTO("Usuario no encontrado", 0, null, null, null, null);
        }

        Cliente cliente = clienteOpt.get();
        if (solicitud.getPassword() == null || !cliente.getPassword().equals(solicitud.getPassword())) {
            return new ClienteDTO("Contraseña incorrecta", 0, null, null, null, null);
        }

        return new ClienteDTO(
                "Login exitoso",
                cliente.getId(),
                cliente.getUsername(),
                null,
                cliente.getEmail(),
                cliente.getTelefonoContacto()
        );
    }

    private Optional<Cliente> buscarPorIdentificador(String identificador) {
        if (identificador == null || identificador.isBlank()) {
            return Optional.empty();
        }

        if (identificador.contains("@")) {
            Optional<Cliente> porEmail = clienteRepository.findByEmail(identificador);
            return porEmail.isPresent() ? porEmail : clienteRepository.findByUsername(identificador);
        }

        Optional<Cliente> porUsername = clienteRepository.findByUsername(identificador);
        return porUsername.isPresent() ? porUsername : clienteRepository.findByEmail(identificador);
    }

    

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public ClienteDTO registrar(ClienteDTO solicitud) {
        if (solicitud.getUsername() == null || solicitud.getUsername().isBlank() ||
            solicitud.getEmail() == null || solicitud.getEmail().isBlank() ||
            solicitud.getPassword() == null || solicitud.getPassword().isBlank()) {
            return new ClienteDTO("Los campos requeridos no pueden estar vacíos.", 0L, null, null, null, null);
        }

        if (clienteRepository.findByUsername(solicitud.getUsername()).isPresent()) {
            return new ClienteDTO("El nombre de usuario ya está registrado.", 0L, null, null, null, null);
        }

        if (clienteRepository.findByEmail(solicitud.getEmail()).isPresent()) {
            return new ClienteDTO("El correo electrónico ya está registrado.", 0L, null, null, null, null);
        }

        Cliente cliente = new Cliente();
        cliente.setUsername(solicitud.getUsername());
        cliente.setEmail(solicitud.getEmail());
        cliente.setPassword(solicitud.getPassword());
        cliente.setTelefonoContacto(solicitud.getTelefonoContacto());

        clienteRepository.save(cliente);

        return new ClienteDTO(
                "Registro exitoso",
                cliente.getId(),
                cliente.getUsername(),
                null,
                cliente.getEmail(),
                cliente.getTelefonoContacto()
        );
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    public ReservaDTO registrarReserva(CrearReservaDTO solicitud) {
        if (solicitud == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud de reserva es obligatoria");
        }

        if (solicitud.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente es obligatorio");
        }

        if (solicitud.getCasaRuralId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La casa rural es obligatoria");
        }

        if (solicitud.getFechaEntrada() == null || solicitud.getFechaSalida() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar la fecha de entrada y salida");
        }

        if (solicitud.getModalidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La modalidad de alquiler es obligatoria");
        }

        LocalDate fechaEntrada = solicitud.getFechaEntrada();
        LocalDate fechaSalida = solicitud.getFechaSalida();

        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de salida debe ser posterior a la fecha de entrada");
        }

        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de entrada no puede ser anterior a hoy");
        }

        Cliente cliente = clienteRepository.findById(solicitud.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con ese identificador"));

        CasaRural casa = casaRuralRepository.findById(solicitud.getCasaRuralId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una casa con ese identificador"));

        if (!Boolean.TRUE.equals(casa.getActiva())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La casa seleccionada no está activa");
        }

        if (!reservaRepository.findConflictosPorCasaYRango(casa.getCodigo(), fechaEntrada, fechaSalida).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La casa no está disponible para el rango de fechas seleccionado");
        }

        long numeroNoches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        if (numeroNoches <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango de fechas debe cubrir al menos una noche");
        }

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.now());
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setNumeroNoches((int) numeroNoches);
        reserva.setModalidad(solicitud.getModalidad());
        reserva.setImporteTotal(BigDecimal.valueOf(casa.getPrecio()).multiply(BigDecimal.valueOf(numeroNoches)));
        reserva.setImporteAnticipo(BigDecimal.ZERO);
        reserva.setFechaLimitePago(fechaEntrada);
        reserva.setEstado(EstadoReserva.PENDIENTE_PAGO);
        reserva.setCasaRural(casa);
        reserva.setCliente(cliente);
        reserva.setPropietario(casa.getPropietario());

        Reserva reservaGuardada = reservaRepository.save(reserva);

        try {
            emailService.enviarReservaCreada(cliente, reservaGuardada);
        } catch (MessagingException e) {
            LOGGER.log(Level.WARNING, "La reserva se creó correctamente pero no se pudo enviar el correo de confirmación.", e);
        }

        return new ReservaDTO(
                reservaGuardada.getNumeroReserva(),
                reservaGuardada.getCasaRural().getCodigo(),
                reservaGuardada.getFechaEntrada(),
                reservaGuardada.getFechaEntrada().plusDays(reservaGuardada.getNumeroNoches()),
                reservaGuardada.getCliente().getTelefonoContacto(),
                reservaGuardada.getImporteTotal(),
                reservaGuardada.getEstado()
        );
    }
}
