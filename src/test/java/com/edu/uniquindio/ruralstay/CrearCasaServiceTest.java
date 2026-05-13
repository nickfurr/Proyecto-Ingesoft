package com.edu.uniquindio.ruralstay;

import com.edu.uniquindio.ruralstay.dto.*;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.entity.enums.TipoCama;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import com.edu.uniquindio.ruralstay.service.CasaRuralService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CrearCasaServiceTest {

    @Autowired
    private CasaRuralService casaRuralService;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Test
    public void testCrearCasaValida() {

        // Crear propietario en BD
        Propietario propietario = new Propietario();
        propietario.setNombreCompleto("Migue");
        propietario.setTelefono("14567");
        propietario.setNumeroCuentaBancaria("172");
        propietario.setActivo(true);
        propietario.setPassword("1234");
        propietario.setUsername(UUID.randomUUID().toString());
        propietario.setEmail(UUID.randomUUID() + "@test.com");

        propietario = propietarioRepository.save(propietario);

        // Crear habitaciones
        HabitacionDTO habitacion1 = new HabitacionDTO(
                null,
                3,
                TipoCama.DOBLE,
                true
        );

        HabitacionDTO habitacion2 = new HabitacionDTO(
                null,
                3,
                TipoCama.SENCILLA,
                false
        );

        HabitacionDTO habitacion3 = new HabitacionDTO(
                null,
                3,
                TipoCama.SENCILLA,
                true
        );

        // Crear baños
        BañoDTO bano1 = new BañoDTO(
                null,
                true
        );

        BañoDTO bano2 = new BañoDTO(
                null,
                false
        );

        // Crear cocinas
        CocinaDTO cocina1 = new CocinaDTO(
                null,
                true,
                true
        );

        // DTO principal
        CrearCasaRuralDTO dto = new CrearCasaRuralDTO();

        dto.setPoblacion("Armenia");
        dto.setDescripcionGeneral("Casa bonita");

        dto.setHabitaciones(List.of(
                habitacion1,
                habitacion2,
                habitacion3
        ));

        dto.setBanos(List.of(
                bano1,
                bano2
        ));

        dto.setCocinas(List.of(
                cocina1
        ));

        dto.setNumeroComedores(1);
        dto.setPlazasGaraje(1);

        dto.setFotos(List.of(
                "https://planner5d.com/blog/es/como-construir-casa/"
        ));

        dto.setPropietarioId(propietario.getId());

        dto.setCiudad("Armenia");
        dto.setPrecio(250000.0);

        // Ejecutar
        CasaRuralDTO resultado = casaRuralService.crearCasa(dto);

        // Validar
        assertNotNull(resultado);
    }
}