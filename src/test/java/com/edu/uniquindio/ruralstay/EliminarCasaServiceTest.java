package com.edu.uniquindio.ruralstay;

import com.edu.uniquindio.ruralstay.entity.CasaRural;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.CasaRuralRepository;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import com.edu.uniquindio.ruralstay.service.CasaRuralService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EliminarCasaServiceTest {

    @Autowired
    private CasaRuralService casaRuralService;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private CasaRuralRepository casaRuralRepository;

    @Test
    public void deberiaEliminarCasaCorrectamente() {

        // 1. Crear propietario
        Propietario propietario = new Propietario();
        propietario.setNombreCompleto("Migue");
        propietario.setTelefono("123");
        propietario.setNumeroCuentaBancaria("456");
        propietario.setActivo(true);
        propietario.setUsername(UUID.randomUUID().toString());
        propietario.setPassword("1234");
        propietario.setEmail(UUID.randomUUID() + "@test.com");

        propietario = propietarioRepository.save(propietario);

        // 2. Crear casa
        CasaRural casa = new CasaRural();
        casa.setPoblacion("Armenia");
        casa.setDescripcionGeneral("Casa test");
        casa.setNumeroDormitorios(3);
        casa.setNumeroBanos(2);
        casa.setNumeroCocinas(1);
        casa.setNumeroComedores(1);
        casa.setPlazasGaraje(1);
        casa.setActiva(true);
        casa.setPropietario(propietario);

        casa = casaRuralRepository.save(casa);

        // 3. Ejecutar eliminación
        casaRuralService.eliminarCasa(casa.getCodigo(), propietario.getId());

        // 4. Verificar
        CasaRural casaActualizada = casaRuralRepository.findById(casa.getCodigo()).orElseThrow();

        assertFalse(casaActualizada.getActiva());
    }

    @Test
    public void noDeberiaEliminarSiNoEsElPropietario() {

        // Propietario 1
        Propietario p1 = new Propietario();
        p1.setNombreCompleto("P1");
        p1.setTelefono("111");
        p1.setNumeroCuentaBancaria("111");
        p1.setActivo(true);
        p1.setUsername(UUID.randomUUID().toString());
        p1.setPassword("1234");
        p1.setEmail(UUID.randomUUID() + "@test.com");

        p1 = propietarioRepository.saveAndFlush(p1);

        // Propietario 2
        Propietario p2 = new Propietario();
        p2.setNombreCompleto("P2");
        p2.setTelefono("222");
        p2.setNumeroCuentaBancaria("222");
        p2.setActivo(true);
        p2.setUsername(UUID.randomUUID().toString());
        p2.setPassword("1234");
        p2.setEmail(UUID.randomUUID() + "@test.com");

        p2 = propietarioRepository.saveAndFlush(p2);

        // Casa de p1
        CasaRural casa = new CasaRural();
        casa.setPoblacion("Armenia");
        casa.setDescripcionGeneral("Casa test");
        casa.setNumeroDormitorios(3);
        casa.setNumeroBanos(2);
        casa.setNumeroCocinas(1);
        casa.setNumeroComedores(1);
        casa.setPlazasGaraje(1);
        casa.setActiva(true);
        casa.setPropietario(p1);

        casa = casaRuralRepository.saveAndFlush(casa);

        CasaRural finalCasa = casa;
        Propietario finalP = p2;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            casaRuralService.eliminarCasa(finalCasa.getCodigo(), finalP.getId());
        });

        // Validar mensaje
        assertTrue(exception.getMessage().contains("No puedes eliminar esta casa"));
    }
}