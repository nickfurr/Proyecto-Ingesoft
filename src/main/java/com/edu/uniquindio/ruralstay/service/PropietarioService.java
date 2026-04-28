package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.PropietarioDTO;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioDTO login(@RequestBody PropietarioDTO solicitud) {
        String identificador = solicitud.getEmail();
        if (identificador == null || identificador.isBlank()) {
            identificador = solicitud.getUsername();
        }

        Optional<Propietario> propietarioOpt = buscarPorIdentificador(identificador);

        if(propietarioOpt.isEmpty()){
            return new PropietarioDTO("Usuario no encontrado",0,null,
                    null,null,null,null,null,null);
        }
        Propietario propietario = propietarioOpt.get();
        if(!propietario.getPassword().equals(solicitud.getPassword())){
            return new PropietarioDTO("Contraseña incorrecta",0,null,
                    null,null,null,null,null,null);
        }
        return new PropietarioDTO(
                "Login exitoso",
                propietario.getId(),
                propietario.getUsername(),
                propietario.getPassword(),
                propietario.getEmail(),
                propietario.getNombreCompleto(),
                propietario.getTelefono(),
                propietario.getNumeroCuentaBancaria(),
                propietario.getActivo()
        );
    }

    private Optional<Propietario> buscarPorIdentificador(String identificador) {
        if (identificador == null || identificador.isBlank()) {
            return Optional.empty();
        }

        if (identificador.contains("@")) {
            Optional<Propietario> porEmail = propietarioRepository.findByEmail(identificador);
            return porEmail.isPresent() ? porEmail : propietarioRepository.findByUsername(identificador);
        }

        Optional<Propietario> porUsername = propietarioRepository.findByUsername(identificador);
        return porUsername.isPresent() ? porUsername : propietarioRepository.findByEmail(identificador);
    }

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    public List<Propietario> listarTodos() {
        return propietarioRepository.findAll();

    }

    public PropietarioDTO buscrPorId(Long id) {
        Optional<Propietario> propietarioOpt = propietarioRepository.findById(id);
        Propietario propietario = propietarioOpt.get();
        return new PropietarioDTO(
                "Usuario encontrado!",
                propietario.getId(),
                propietario.getUsername(),
                propietario.getPassword(),
                propietario.getEmail(),
                propietario.getNombreCompleto(),
                propietario.getTelefono(),
                propietario.getNumeroCuentaBancaria(),
                propietario.getActivo()
        );
    }

    public Propietario guardar(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    public void eliminar(Long id) {
        propietarioRepository.deleteById(id);
    }
}
