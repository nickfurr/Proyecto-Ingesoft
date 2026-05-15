package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Pruebas para GestionReserva")
class GestionReservaTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private GestionReserva gestionReserva;

    private Propietario propietario;
    private Propietario otroPropietario;
    private Reserva reserva1;
    private Reserva reserva2;
    private Reserva reserva3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear propietarios
        propietario = new Propietario();
        propietario.setId(1L);
        propietario.setUsername("propietario1");
        propietario.setNombreCompleto("Juan Pérez");
        propietario.setTelefono("3001234567");

        otroPropietario = new Propietario();
        otroPropietario.setId(2L);
        otroPropietario.setUsername("propietario2");
        otroPropietario.setNombreCompleto("María García");
        otroPropietario.setTelefono("3007654321");

        // Crear reservas del propietario 1
        reserva1 = new Reserva();
        reserva1.setNumeroReserva(1L);
        reserva1.setFechaReserva(LocalDate.now());
        reserva1.setFechaEntrada(LocalDate.now().plusDays(5));
        reserva1.setNumeroNoches(3);
        reserva1.setImporteTotal(new BigDecimal("150.00"));
        reserva1.setImporteAnticipo(new BigDecimal("50.00"));
        reserva1.setEstado(EstadoReserva.PENDIENTE_PAGO);
        reserva1.setPropietario(propietario);

        reserva2 = new Reserva();
        reserva2.setNumeroReserva(2L);
        reserva2.setFechaReserva(LocalDate.now());
        reserva2.setFechaEntrada(LocalDate.now().plusDays(10));
        reserva2.setNumeroNoches(4);
        reserva2.setImporteTotal(new BigDecimal("200.00"));
        reserva2.setImporteAnticipo(new BigDecimal("60.00"));
        reserva2.setEstado(EstadoReserva.PENDIENTE_PAGO);
        reserva2.setPropietario(propietario);

        // Crear reserva de otro propietario
        reserva3 = new Reserva();
        reserva3.setNumeroReserva(3L);
        reserva3.setFechaReserva(LocalDate.now());
        reserva3.setFechaEntrada(LocalDate.now().plusDays(15));
        reserva3.setNumeroNoches(2);
        reserva3.setImporteTotal(new BigDecimal("100.00"));
        reserva3.setImporteAnticipo(new BigDecimal("40.00"));
        reserva3.setEstado(EstadoReserva.CONFIRMADA);
        reserva3.setPropietario(otroPropietario);
    }

    @Test
    @DisplayName("Listar reservas del propietario retorna solo sus reservas")
    void testListarReservasPropietario() {
        // Arrange
        List<Reserva> reservasDelPropietario = Arrays.asList(reserva1, reserva2);
        when(reservaRepository.findByPropietarioId(1L)).thenReturn(reservasDelPropietario);

        // Act
        List<Reserva> resultado = gestionReserva.listarReservasPropietario(propietario);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(reserva1));
        assertTrue(resultado.contains(reserva2));
        verify(reservaRepository, times(1)).findByPropietarioId(1L);
    }

    @Test
    @DisplayName("Listar reservas retorna lista vacía cuando no hay reservas del propietario")
    void testListarReservasPropietarioVacio() {
        // Arrange
        when(reservaRepository.findByPropietarioId(1L)).thenReturn(Arrays.asList());

        // Act
        List<Reserva> resultado = gestionReserva.listarReservasPropietario(propietario);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(reservaRepository, times(1)).findByPropietarioId(1L);
    }

    @Test
    @DisplayName("Confirmar pago inicial actualiza el estado a PAGADA_PARCIAL")
    void testConfirmarPagoInicialReserva() {
        // Arrange
        BigDecimal montoAnticipo = new BigDecimal("75.00");
        when(reservaRepository.findByNumeroReserva(1L)).thenReturn(Optional.of(reserva1));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva1);

        // Act
        gestionReserva.confirmarPagoInicialReserva(1L, montoAnticipo, propietario);

        // Assert
        assertEquals(EstadoReserva.PAGADA_PARCIAL, reserva1.getEstado());
        assertEquals(montoAnticipo, reserva1.getImporteAnticipo());
        verify(reservaRepository, times(1)).save(reserva1);
    }

    @Test
    @DisplayName("Confirmar pago lanza excepción cuando la reserva no existe")
    void testConfirmarPagoInicialReservaNoExiste() {
        // Arrange
        BigDecimal montoAnticipo = new BigDecimal("75.00");
        when(reservaRepository.findByNumeroReserva(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> 
            gestionReserva.confirmarPagoInicialReserva(100L, montoAnticipo, propietario)
        );
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Confirmar pago lanza excepción cuando propietario no coincide")
    void testConfirmarPagoInicialReservaPropietarioIncorrecto() {
        // Arrange
        BigDecimal montoAnticipo = new BigDecimal("75.00");
        when(reservaRepository.findByNumeroReserva(1L)).thenReturn(Optional.of(reserva1));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> 
            gestionReserva.confirmarPagoInicialReserva(1L, montoAnticipo, otroPropietario)
        );
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Confirmar pago actualiza el monto del anticipo")
    void testConfirmarPagoActualizaMonto() {
        // Arrange
        BigDecimal montoNuevo = new BigDecimal("100.00");
        when(reservaRepository.findByNumeroReserva(1L)).thenReturn(Optional.of(reserva1));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva1);

        // Act
        gestionReserva.confirmarPagoInicialReserva(1L, montoNuevo, propietario);

        // Assert
        assertEquals(montoNuevo, reserva1.getImporteAnticipo());
        verify(reservaRepository, times(1)).save(reserva1);
    }

    @Test
    @DisplayName("No confirma pago cuando el propietario no es dueño de la reserva")
    void testNoConfirmarPagoConPropietarioNoAutorizado() {
        // Arrange - reserva1 pertenece a propietario (ID=1) pero intentamos confirmar con otroPropietario (ID=2)
        BigDecimal montoAnticipo = new BigDecimal("50.00");
        when(reservaRepository.findByNumeroReserva(1L)).thenReturn(Optional.of(reserva1));

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> 
            gestionReserva.confirmarPagoInicialReserva(1L, montoAnticipo, otroPropietario)
        );
        
        // Verifica que el mensaje sea el correcto
        assertEquals("El propietario no es el de la reserva", exception.getMessage());
        
        // Verifica que NO se llamó a save (no se guarda la reserva)
        verify(reservaRepository, never()).save(any());
        
        // Verifica que el estado de la reserva no cambió
        assertNotEquals(EstadoReserva.PAGADA_PARCIAL, reserva1.getEstado());
    }
}
