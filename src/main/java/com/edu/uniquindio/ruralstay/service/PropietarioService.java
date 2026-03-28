package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.Cliente;
import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    public List<Propietario> listarTodos() {
        return propietarioRepository.findAll();

    }

    public Optional<Propietario> buscrPorId(Long id) {

        return propietarioRepository.findById(id);
    }

    public Propietario guardar(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    public void eliminar(Long id) {
        propietarioRepository.deleteById(id);
    }
}
