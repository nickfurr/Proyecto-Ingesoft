package com.edu.uniquindio.ruralstay;

import com.edu.uniquindio.ruralstay.dto.HistoricoPaqueteDTO;
import com.edu.uniquindio.ruralstay.entity.*;
import com.edu.uniquindio.ruralstay.entity.enums.EstadoReserva;
import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import com.edu.uniquindio.ruralstay.repository.*;
import com.edu.uniquindio.ruralstay.service.PaqueteAlquilerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaqueteAlquilerServiceTest {

        @Autowired
        private PaqueteAlquilerService service;

        @Autowired
        private PropietarioRepository propietarioRepository;

        @Autowired
        private CasaRuralRepository casaRuralRepository;

        @Autowired
        private PaqueteAlquilerRepository paqueteRepository;

        @Autowired
        private ReservaRepository reservaRepository;
        @Autowired
        private ClienteRepository clienteRepository;

        @Test
        void deberiaObtenerHistoricoConDatos() {

                // Crear cliente
                Cliente cliente = new Cliente();
                cliente.setUsername("cliente1");
                cliente.setPassword("1234");
                cliente.setTelefonoContacto("30292834");
                cliente.setEmail("cliente1@test.com");
                cliente = clienteRepository.saveAndFlush(cliente);

                // 🔹 1. Crear propietario
                Propietario propietario = new Propietario();
                propietario.setNombreCompleto("Juan");
                propietario.setTelefono("3114552");
                propietario.setNumeroCuentaBancaria("1234");
                propietario.setActivo(true);
                propietario.setUsername("juan123");
                propietario.setPassword("1234");
                propietario.setEmail("juan@test.com");
                propietario = propietarioRepository.save(propietario);

                // 🔹 2. Crear casa rural
                CasaRural casa = new CasaRural();
                casa.setPoblacion("Familia");
                casa.setNumeroDormitorios(6);
                casa.setNumeroBanos(2);
                casa.setNumeroCocinas(1);
                casa.setNumeroComedores(2);
                casa.setPlazasGaraje(1);
                casa.setActiva(true);
                casa.setPropietario(propietario);
                casa = casaRuralRepository.saveAndFlush(casa);

                // 🔹 3. Crear paquete
                PaqueteAlquiler paquete = new PaqueteAlquiler();
                paquete.setCasaRural(casa);
                paquete.setPrecioCasaEntera(new BigDecimal("500000"));
                paquete.setModalidad(ModalidadAlquiler.CASA_ENTERA);
                paquete.setFechaInicio(LocalDate.of(2023, 1, 1));
                paquete.setFechaFin(LocalDate.of(2023, 12, 31));
                paquete.setVigente(true);
                paquete = paqueteRepository.saveAndFlush(paquete);

                // 🔹 4. Crear reserva
                Reserva reserva = new Reserva();
                reserva.setCasaRural(casa);
                reserva.setCliente(cliente); // ⚡ importante
                reserva.setPropietario(propietario); // ⚡ si tu entidad lo requiere
                reserva.setFechaEntrada(LocalDate.of(2023, 6, 1));
                reserva.setFechaReserva(LocalDate.of(2023, 5, 20)); // campo NOT NULL
                reserva.setFechaLimitePago(LocalDate.of(2023, 5, 25)); // campo NOT NULL
                reserva.setImporteTotal(BigDecimal.valueOf(200000.0));
                reserva.setImporteAnticipo(BigDecimal.valueOf(100000.0)); // campo NOT NULL
                reserva.setNumeroNoches(5); // campo NOT NULL
                reserva.setModalidad(ModalidadAlquiler.CASA_ENTERA); // si aplica
                reserva.setEstado(EstadoReserva.CONFIRMADA); // enum obligatorio

                reservaRepository.saveAndFlush(reserva);

                // 🔹 5. Ejecutar query
                List<HistoricoPaqueteDTO> resultado = service.obtenerHistorico(
                                propietario.getId(),
                                LocalDate.of(2023, 1, 1),
                                LocalDate.of(2023, 12, 31));

                // 🔹 6. Validaciones
                assertNotNull(resultado);
                assertFalse(resultado.isEmpty());

                HistoricoPaqueteDTO dto = resultado.get(0);

                assertEquals(paquete.getId(), dto.getIdPaquete());
                assertEquals(1L, dto.getTotalReservas());
                assertEquals(200000.0, dto.getIngresos().doubleValue(), 0.01);
        }

        @Test
        void deberiaRetornarListaVaciaSiNoHayDatos() {

                // Crear un propietario en la base de datos H2
                Propietario propietario = new Propietario();
                propietario.setActivo(true);
                propietario.setNombreCompleto("Propietario prueba");
                propietario.setNumeroCuentaBancaria("123456");
                propietario.setTelefono("3001234567");
                propietario.setPassword("1234");
                propietario.setUsername("propietarioPrueba");
                propietario.setEmail("propietario@test.com");
                propietario = propietarioRepository.save(propietario); // ID se genera automáticamente

                List<HistoricoPaqueteDTO> resultado = service.obtenerHistorico(
                                propietario.getId(), // usa el ID generado
                                LocalDate.of(2023, 1, 1),
                                LocalDate.of(2023, 12, 31));

                assertNotNull(resultado);
                assertTrue(resultado.isEmpty());
        }
}