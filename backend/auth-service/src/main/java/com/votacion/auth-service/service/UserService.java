package com.votacion.auth_service.service;

import com.votacion.auth_service.model.User;
import java.util.Optional;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

// Esta interface define los métodos que el servicio de usuario debe implementar.
// Se utiliza para separar la lógica de negocio de la implementación concreta.
public interface UserService {

    User saveUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByDni(String dni);

    List<User> getAllUsers();

    Boolean hasVoted(Long userId);

    void disableUser(Long id);

    void resetFailedAttempts(String email);

    List<User> registerVotersFromCsv(MultipartFile file) throws Exception;
}
