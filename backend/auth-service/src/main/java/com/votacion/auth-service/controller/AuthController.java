package com.votacion.auth_service.controller;

import com.votacion.auth_service.dto.LoginRequest;
import com.votacion.auth_service.dto.RegisterRequest;
import com.votacion.auth_service.dto.AuthResponse;
import com.votacion.auth_service.model.User;
import com.votacion.auth_service.model.Role;
import com.votacion.auth_service.repository.RoleRepository;
import com.votacion.auth_service.service.AuthService;
import com.votacion.auth_service.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Endpoint de login: genera token si las credenciales son válidas - FUNCIONAL
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Se invocó /auth/login con: " + loginRequest.getIdentifier());

            AuthResponse response = authService.login(
                loginRequest.getIdentifier(),
                loginRequest.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // para que salga en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Endpoint para ver el perfil del usuario autenticado - FUNCIONAL
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autorizado"));
        }

        String email = authentication.getName(); // El "subject" del JWT

        return userService.findByEmail(email)
            .map(user -> ResponseEntity.ok(
                Map.of(
                    "email", user.getEmail(),
                    "dni", user.getDni(),
                    "roles", user.getRoles().stream().map(Role::getName).toList()
                )))
            .orElse(ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado")));
    }

    // Endpoint para registrar solo admins (protegido) - EN PRUEBAS
    // Error 403 Forbidden ;-;
    @PostMapping("/register")
    //PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un usuario con este email.");
        }

        Set<Role> roles = request.getRoles().stream()
            .map(roleName -> roleRepository.findByName(roleName))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // Solo permitir admins
        if (roles.size() != 1 || !roles.iterator().next().getName().equals("ROLE_ADMIN")) {
            return ResponseEntity.badRequest().body("Solo se pueden registrar usuarios con rol ADMIN.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria para admins.");
        }

        User user = new User(
            request.getNombre(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getDni()
        );
        user.setRoles(roles);
        userService.saveUser(user);

        return ResponseEntity.ok("Admin registrado exitosamente.");
    }

    // Endpoint para verificar si un usuario ya votó (consultado por vote-service) - EN ESPERA
    @GetMapping("/status/{userDni}")
    public ResponseEntity<?> checkUserVoteStatus(@PathVariable String userDni) {
        Optional<User> userOpt = userService.findByDni(userDni);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
        }
        User user = userOpt.get();
        boolean hasVoted = user.isHasVoted(); // Ajusta este método según tu modelo
        return ResponseEntity.ok(Map.of(
            "userDni", userDni,
            "hasVoted", hasVoted
        ));
    }

    // Endpoint para listar todos los usuarios (solo para admins) - FUNCIONAL
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Endpoint de prueba para verificar si el servicio está activo - FUNCIONAL
    @GetMapping({"/", ""})
    public String home() {
        return "Auth Service activo ✅";
    }

    // Endpoint para registrar votantes desde un CSV - EN PRUEBAS
    @PostMapping("/register-csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerVotersCsv(@RequestParam("file") MultipartFile file) {
        try {
            // Aquí deberías procesar el CSV, crear usuarios con rol VOTANTE,
            // generar votingCode y guardarlos.
            // Ejemplo de lógica simplificada:
            List<User> nuevosVotantes = userService.registerVotersFromCsv(file);
            return ResponseEntity.ok("Votantes registrados: " + nuevosVotantes.size());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo CSV.");
        }
    }


    // Endpoint para enviar codigos - no probado
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCodeToVoter(@RequestBody Map<String, String> body) {
        String dni = body.get("dni");
        Optional<User> userOpt = userService.findByDni(dni);

        if (userOpt.isPresent() && userOpt.get().hasRole("ROLE_VOTANTE")) {
            String code = String.format("%06d", new Random().nextInt(999999));
            User user = userOpt.get();
            user.setVotingCode(code);
            userService.saveUser(user);

            // Aquí simulas el envío por correo o logueas (luego puedes integrar una librería real)
            System.out.println("🔐 Código enviado a " + user.getEmail() + ": " + code);

            return ResponseEntity.ok("Código enviado exitosamente");
        }

        return ResponseEntity.badRequest().body("Votante no encontrado o sin permisos");
    }

    // Endpoint login especial para votantes - no probado
    @PostMapping("/votar")
    public ResponseEntity<?> loginVoter(@RequestBody Map<String, String> login) {
        String dni = login.get("dni");
        String codigo = login.get("codigo");

        Optional<User> userOpt = userService.findByDni(dni);
        if (userOpt.isPresent() && userOpt.get().getVotingCode().equals(codigo)) {
            String token = authService.generateToken(userOpt.get());
            return ResponseEntity.ok(new AuthResponse(token, userOpt.get().getEmail(), userOpt.get().getName()));
        }

        return ResponseEntity.status(401).body("DNI o código incorrecto");
    }
}
