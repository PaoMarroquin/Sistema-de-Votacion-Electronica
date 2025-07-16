package com.votacion.auth_service.service;

public interface EmailService {
    void enviarCodigo(String destinatario, String codigo);
}
