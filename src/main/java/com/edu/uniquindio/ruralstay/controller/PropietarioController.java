package com.edu.uniquindio.ruralstay.controller;

import com.edu.uniquindio.ruralstay.entity.Propietario;
import com.edu.uniquindio.ruralstay.repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class PropietarioController {
    @Autowired
    private PropietarioRepository propietarioRepository;

    @GetMapping("/propietarios")
    public List<Propietario> listarTodosPropietarios() {
        return propietarioRepository.findAll();
    }
}
