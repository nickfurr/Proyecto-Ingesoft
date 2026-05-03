package com.edu.uniquindio.ruralstay;

import com.edu.uniquindio.ruralstay.dto.CasaRuralDTO;
import com.edu.uniquindio.ruralstay.dto.CrearCasaRuralDTO;
import com.edu.uniquindio.ruralstay.entity.Propietario;
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

        // DTO
        CrearCasaRuralDTO dto = new CrearCasaRuralDTO();
        dto.setPoblacion("Armenia");
        dto.setDescripcionGeneral("Casa bonita");
        dto.setNumeroDormitorios(3);
        dto.setNumeroBanos(2);
        dto.setNumeroCocinas(1);
        dto.setNumeroComedores(1);
        dto.setPlazasGaraje(1);
        dto.setFotos(List.of(
                "https://planner5d.com/blog/es/como-construir-casa/"
        ));
        dto.setPropietarioId(propietario.getId());

        // Ejecutar
        CasaRuralDTO resultado = casaRuralService.crearCasa(dto);

        // Validar
        assertNotNull(resultado);
    }
}