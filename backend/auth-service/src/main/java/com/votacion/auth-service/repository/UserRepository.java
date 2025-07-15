package com.votacion.auth_service.repository;

import com.votacion.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Esta interfaz extiende JpaRepository para proporcionar métodos CRUD básicos
// Se utiliza para la autenticación y autorización de usuarios en el sistema.
// Además, se utiliza para la gestión de usuarios en el sistema de votación.

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Define métodos personalizados para buscar usuarios por email, DNI o ambos,
    Optional<User> findByEmail(String email);

    Optional<User> findByDni(String dni);

    Optional<User> findByEmailOrDni(String email, String dni);

    // y verificar la existencia de un usuario por email o DNI.
    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}
