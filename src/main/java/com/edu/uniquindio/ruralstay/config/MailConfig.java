package com.edu.uniquindio.ruralstay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Bean mínimo para evitar fallos en tiempo de arranque en entornos de desarrollo.
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        // No se configuran host/credenciales aquí; en producción usar propiedades reales.
        return sender;
    }
}
