package com.edu.uniquindio.ruralstay.service;

import com.edu.uniquindio.ruralstay.dto.ClienteDTO;
import com.edu.uniquindio.ruralstay.entity.Cliente;
import com.edu.uniquindio.ruralstay.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
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

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}
