package com.votacion.auth_service.controller;

import com.votacion.auth_service.dto.LoginRequest;
import com.votacion.auth_service.dto.RegisterRequest;
import com.votacion.auth_service.dto.AuthResponse;
import com.votacion.auth_service.model.User;
import com.votacion.auth_service.model.Role;
import com.votacion.auth_service.repository.RoleRepository;
import com.votacion.auth_service.service.AuthService;
import com.votacion.auth_service.service.EmailService;
import com.votacion.auth_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;
import java.util.stream.Collectors;

// Endpoints implementados en este controlador auth-service:
// - /auth: Endpoint de prueba para verificar si el servicio está activo - FUNCIONAL
// - /auth/login: Login de usuario (genera token) - FUNCIONAL
// - /auth/profile: Obtiene perfil y datos del usuario autenticado - FUNCIONAL
// - /auth/register: Registra un nuevo admin (solo admins pueden invocar) - FUNCIONAL
// - /auth/users: Lista todos los usuarios (solo admins) - (YA NO SE USA, se reemplazado por /auth/users/admins y /auth/users/votantes)
// - /auth/users/admins: Lista todos los admins (solo admins) - FUNCIONAL
// - /auth/users/votantes: Lista todos los votantes (solo admins) - FUNCIONAL
// - /auth/register-csv: Registra votantes desde un archivo CSV (solo admins) - FUNCIONAL
// - /auth/send-code-test: Envía código de votación a un votante por email (solo admins) - FUNCIONAL
// - /auth/send-codes-test: Envía códigos de votación a todos los votantes (simulado, solo admins) - EN PRUEBAS
// - /auth/send-codes: Envía códigos de votación a todos los votantes (real, solo admins) - EN PRUEBAS
// - /auth/votar: Login especial para votantes (verifica DNI y código de votación) - FUNCIONAL

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private EmailService emailService;

    // Endpoint de prueba para verificar si el servicio está activo - FUNCIONAL
    @GetMapping({"/", ""})
    public String home() {
        return "Auth Service activo ✅";
    }

    // Endpoint de login: genera token si las credenciales son válidas - FUNCIONAL
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("✅ Se invocó /auth/login con: " + loginRequest.getIdentifier());

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

    // Endpoint para registrar un nuevo admin - FUNCIONAL
    // Solo puede ser invocado por un admin autenticado
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> registerAdmin(@RequestBody RegisterRequest request) {

        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con este email."));
        }

        Set<Role> roles = request.getRoles().stream()
            .map(roleName -> roleRepository.findByName(roleName))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (roles.size() != 1 || !roles.iterator().next().getName().equals("ROLE_ADMIN")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Solo se pueden registrar usuarios con rol ADMIN."));
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "La contraseña es obligatoria para admins."));
        }

        // Crear y guardar el usuario
        User user = new User(
            request.getNombre(),
            request.getEmail(),
            request.getPassword(),
            request.getDni()
        );
        user.setRoles(roles);
        userService.saveUser(user);

        // Preparar la respuesta en formato JSON
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", "✅ Admin registrado exitosamente.");
        response.put("nombre", user.getName());
        response.put("email", user.getEmail());
        response.put("dni", user.getDni());
        //response.put("contraseña_plana", request.getPassword());
        response.put("roles", roles.stream().map(Role::getName).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    // Endpoint para listar todos los usuarios (solo para admins) - FUNCIONAL
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/admins")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listarAdmins() {
        return ResponseEntity.ok(userService.getUsersByRole("ROLE_ADMIN"));
    }

    @GetMapping("/users/votantes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listarVotantes() {
        return ResponseEntity.ok(userService.getUsersByRole("ROLE_VOTANTE"));
    }

    // Endpoint para registrar votantes desde un CSV - FUNCIONAL
    @PostMapping("/register-csv") //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerVotersCsv(@RequestParam("file") MultipartFile file) {
        log.info("📥 Se recibió solicitud para registrar votantes desde archivo CSV: {}", file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                log.warn("⚠️ El archivo CSV está vacío.");
                return ResponseEntity.badRequest().body("El archivo CSV está vacío.");
            }

            List<User> nuevosVotantes = userService.registerVotersFromCsv(file);
            log.info("✅ Votantes registrados exitosamente. Total: {}", nuevosVotantes.size());

            for (User votante : nuevosVotantes) {
                log.debug("🆕 Votante registrado: ID={}, Email={}, DNI={}, CódigoVotación={}", 
                    votante.getId(), votante.getEmail(), votante.getDni(), votante.getVotingCode());
            }

            return ResponseEntity.ok("Votantes registrados: " + nuevosVotantes.size());

        } catch (Exception e) {
            log.error("❌ Error al procesar archivo CSV: {}", e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar el archivo CSV.");
        }
    }
 
    // Este endpoint envía un código de votación a un votante por email (simulado) - FUNCIONAL
    // Solo puede ser invocado por un admin autenticado
    @PostMapping("/send-code-test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    // Este endpoint envía códigos de votación a todos los votantes (simulado) - EN PRUEBAS
    @PostMapping("/send-codes-test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendCodesToAllVotersTest() {
        List<User> votantes = userService.getUsersByRole("ROLE_VOTANTE"); // asegúrate de tener este método
        List<String> correosEnviados = new ArrayList<>();

        for (User user : votantes) {
            String code = String.format("%06d", new Random().nextInt(999999));
            user.setVotingCode(code);
            userService.saveUser(user);

            // Simulación de envío real
            System.out.println("🔐 Código enviado a " + user.getEmail() + ": " + code);
            correosEnviados.add(user.getEmail());
        }

        return ResponseEntity.ok("Códigos enviados a " + correosEnviados.size() + " votantes.");
    }

    // Este endpoint envía códigos de votación a todos los votantes (real) - EN PRUEBAS
    @PostMapping("/send-codes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendCodesToAllVoters() {
        List<User> votantes = userService.getUsersByRole("ROLE_VOTANTE");
        List<String> correosEnviados = new ArrayList<>();

        for (User user : votantes) {
            String code = String.format("%06d", new Random().nextInt(999999));
            user.setVotingCode(code);
            userService.saveUser(user);

            // Envío real del correo
            emailService.enviarCodigo(user.getEmail(), code);
            correosEnviados.add(user.getEmail());
        }

        return ResponseEntity.ok("Códigos enviados a " + correosEnviados.size() + " votantes.");
    }


    // Endpoint login publico especial para votantes - FUNCIONAL
    // Este endpoint permite a un votante autenticarse usando su DNI y código de votación
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
