package com.votacion.auth_service.service;

import com.votacion.auth_service.model.User;
import com.votacion.auth_service.repository.UserRepository;
import com.votacion.auth_service.security.JwtService;
import org.springframework.web.multipart.MultipartFile;
import com.votacion.auth_service.model.Role;
import com.votacion.auth_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// Esta clase implementa la lógica de negocio para el servicio de usuario.
// Utiliza un repositorio para acceder a los datos de usuario y un codificador de contraseñas para manejar la seguridad.
// También utiliza un servicio JWT para manejar la autenticación basada en tokens.

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void disableUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActivo(false);
            userRepository.save(user);
        });
    }

    @Override
    public void resetFailedAttempts(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setIntentosFallidos(0);
            userRepository.save(user);
        });
    }

    @Override
    public Boolean hasVoted(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::isHasVoted).orElse(false);
    }

    @Override
    public List<User> registerVotersFromCsv(MultipartFile file) throws Exception {
        List<User> nuevosVotantes = new ArrayList<>();
        Role rolVotante = roleRepository.findByName("ROLE_VOTANTE");
        if (rolVotante == null) {
            throw new RuntimeException("El rol ROLE_VOTANTE no existe en la base de datos.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { // Saltar encabezado
                    firstLine = false;
                    continue;
                }
                String[] datos = line.split(",");
                if (datos.length < 3) continue; // nombre,email,dni

                String nombre = datos[0].trim();
                String email = datos[1].trim();
                String dni = datos[2].trim();

                // Evitar duplicados
                if (userRepository.findByEmail(email).isPresent() || userRepository.findByDni(dni).isPresent()) {
                    continue;
                }

                // Generar código de votación (6 dígitos aleatorios)
                String votingCode = String.valueOf((int)(Math.random() * 900000) + 100000);

                User user = new User();
                user.setName(nombre);
                user.setEmail(email);
                user.setDni(dni);
                user.setVotingCode(votingCode);
                user.setActivo(true);
                user.setRoles(Set.of(rolVotante));
                user.setHasVoted(false);
                user.setPassword(null); // No tiene contraseña

                nuevosVotantes.add(userRepository.save(user));
            }
        }
        return nuevosVotantes;
    }
}
