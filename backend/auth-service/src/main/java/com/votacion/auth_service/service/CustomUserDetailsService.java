package com.votacion.auth_service.service;

import com.votacion.auth_service.model.User;
import com.votacion.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.println("🧠 Ejecutando loadUserByUsername con: " + identifier); // DEBUG

        
        // Buscar por email o DNI
        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByDni(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Convertir los roles a GrantedAuthority
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        log.info("🔐 Granted authorities cargadas: {}", authorities);


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // puede usar también user.getDni()
                user.getPassword(),
                user.isActivo(), // enabled
                true,            // accountNonExpired
                true,            // credentialsNonExpired
                true,            // accountNonLocked
                authorities
        );
    }
}
