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
            return new PropietarioDTO("Usuario no encontrado",null,null,
                    null,null,null,null,null,null);
        }
        Propietario propietario = propietarioOpt.get();
        if(!propietario.getPassword().equals(solicitud.getPassword())){
            return new PropietarioDTO("Contraseña incorrecta",null,null,
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

    public PropietarioDTO registrar(PropietarioDTO solicitud) {
        if (solicitud.getUsername() == null || solicitud.getUsername().isBlank()) {
            return new PropietarioDTO("El nombre de usuario es obligatorio", null, null, null, null, null, null, null, null);
        }
        if (propietarioRepository.findByUsername(solicitud.getUsername()).isPresent()) {
            return new PropietarioDTO("El nombre de usuario ya está en uso", null, null, null, null, null, null, null, null);
        }
        if (solicitud.getEmail() != null && !solicitud.getEmail().isBlank()
                && propietarioRepository.findByEmail(solicitud.getEmail()).isPresent()) {
            return new PropietarioDTO("El correo electrónico ya está en uso", null, null, null, null, null, null, null, null);
        }

        Propietario propietario = new Propietario();
        propietario.setUsername(solicitud.getUsername());
        propietario.setPassword(solicitud.getPassword());
        propietario.setEmail(solicitud.getEmail());
        propietario.setNombreCompleto(solicitud.getNombreCompleto() != null ? solicitud.getNombreCompleto() : solicitud.getUsername());
        propietario.setTelefono(solicitud.getTelefono() != null ? solicitud.getTelefono() : "");
        propietario.setNumeroCuentaBancaria(solicitud.getNumeroCuentaBancaria() != null ? solicitud.getNumeroCuentaBancaria() : "");
        propietario.setActivo(true);

        Propietario saved = propietarioRepository.save(propietario);
        return new PropietarioDTO("Registro exitoso", saved.getId(), saved.getUsername(), null,
                saved.getEmail(), saved.getNombreCompleto(), saved.getTelefono(),
                saved.getNumeroCuentaBancaria(), saved.getActivo());
    }

    public Propietario guardar(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    public void eliminar(Long id) {
        propietarioRepository.deleteById(id);
    }
}
