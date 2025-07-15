package com.votacion.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase representa la respuesta de autenticación que se envía al cliente después de un inicio de sesión exitoso.
// Contiene el token JWT, el correo electrónico y el nombre del usuario autenticado.
// Se utiliza para enviar información relevante al cliente de manera estructurada.

// Las anotaciones @Data, @AllArgsConstructor y @NoArgsConstructor de Lombok
// generan automáticamente los métodos getter, setter, un constructor con todos los campos y un constructor sin parámetros.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String nombre;
}

