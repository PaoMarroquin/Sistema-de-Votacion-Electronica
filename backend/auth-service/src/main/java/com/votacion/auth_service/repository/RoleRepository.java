package com.votacion.auth_service.repository;

import com.votacion.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

// Esta interface define un repositorio para la entidad Role,
// permitiendo operaciones CRUD y consultas personalizadas.
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Método para encontrar un rol por su nombre.
    // Devuelve un objeto Role si se encuentra, o null si no existe.
    Role findByName(String name);
}
