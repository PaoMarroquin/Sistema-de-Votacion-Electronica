package com.votacion.auth_service.util;

import com.votacion.auth_service.model.Role;
import com.votacion.auth_service.model.User;
import com.votacion.auth_service.repository.RoleRepository;
import com.votacion.auth_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartupDataLoader {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Crear roles si no existen
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_VOTANTE");

        // Crear usuario admin por defecto
        createDefaultAdmin();
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            System.out.println("Rol creado: " + roleName);
        }
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@voto.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User admin = new User(
                    "Administrador",
                    adminEmail,
                    passwordEncoder.encode("admin123"),
                    "000000000"
            );
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("Usuario admin creado: " + adminEmail);
        }
    }
}
