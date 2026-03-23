package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.entity.PaqueteAlquiler;
import com.edu.uniquindio.ruralstay.repository.PaqueteAlquilerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaqueteAlquilerService {

    private final PaqueteAlquilerRepository paqueteAlquilerRepository;

    public PaqueteAlquilerService(PaqueteAlquilerRepository paqueteAlquilerRepository) {
        this.paqueteAlquilerRepository = paqueteAlquilerRepository;
    }

    public List<PaqueteAlquiler> listarTodos() {
        return paqueteAlquilerRepository.findAll();
    }

    public Optional<PaqueteAlquiler> buscarPorId(Long id) {
        return paqueteAlquilerRepository.findById(id);
    }

    public PaqueteAlquiler guardar(PaqueteAlquiler paqueteAlquiler) {
        return paqueteAlquilerRepository.save(paqueteAlquiler);
    }

    public void eliminar(Long id) {
        paqueteAlquilerRepository.deleteById(id);
    }
}
