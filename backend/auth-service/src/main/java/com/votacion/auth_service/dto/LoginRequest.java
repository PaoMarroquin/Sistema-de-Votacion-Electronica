package com.votacion.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase representa la solicitud de inicio de sesión que se envía al servidor.
// Contiene el identificador del usuario (que puede ser un correo electrónico o un DNI)
// y la contraseña. Se utiliza para autenticar al usuario en el sistema.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String identifier; // Puede ser email o DNI
    private String password;
}
