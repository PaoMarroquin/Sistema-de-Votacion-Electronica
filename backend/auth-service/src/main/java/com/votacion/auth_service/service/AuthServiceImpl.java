package com.votacion.auth_service.service;

import com.votacion.auth_service.dto.AuthResponse;
import com.votacion.auth_service.model.User;
import com.votacion.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(String identifier, String password) {
        log.info("⌛Intentando iniciar sesión con identificador: {}", identifier);

        Optional<User> userOpt = userService.findByEmail(identifier);

        // Si no encontró por email, busca por DNI
        if (userOpt.isEmpty()) {
            userOpt = userService.findByDni(identifier);
            log.info("🔍 Buscando usuario por DNI: {}", identifier);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (!user.isActivo()) {
                log.warn("❌ Usuario inactivo: {}", identifier);
                throw new RuntimeException("Usuario inactivo.");
            }

            log.info("🔍 Contraseña ingresada: {}", password);
            log.info("🔍 Contraseña ingresada encriptada: {}", passwordEncoder.encode(password));
            log.info("🔐 Contraseña en BD: {}", user.getPassword());
            log.info("🔁 Resultado de comparación: {}", passwordEncoder.matches(password, user.getPassword()));


            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtService.generateToken(user);
                log.info("✅ Inicio de sesión exitoso para: {}", identifier);
                return new AuthResponse(token, user.getEmail(), user.getName());
            } else {
                log.warn("❌ Contraseña incorrecta para: {}", identifier);
                throw new RuntimeException("❌ Credenciales inválidas");

            }
        } else {
            log.warn("❌ Usuario no encontrado con identificador: {}", identifier);
        }

        throw new RuntimeException("❌ Credenciales inválidas");
    }

    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }
}
