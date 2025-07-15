package com.votacion.auth_service.service;

import com.votacion.auth_service.dto.AuthResponse;
//import com.votacion.auth_service.dto.LoginRequest;
import com.votacion.auth_service.model.User;

public interface AuthService {
    AuthResponse login(String identifier, String password);

    String generateToken(User user);
}


