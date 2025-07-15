package com.votacion.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// Esta clase representa la solicitud de registro que se envía al servidor.
// Contiene el nombre, correo electrónico, contraseña, DNI y roles del usuario.
// Se utiliza para registrar un nuevo usuario en el sistema.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private String dni;
    private Set<String> roles; // Por ejemplo: ["ROLE_USER"] o ["ROLE_ADMIN"]
}
