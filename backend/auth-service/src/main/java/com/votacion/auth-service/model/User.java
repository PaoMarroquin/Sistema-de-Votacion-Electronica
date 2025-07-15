package com.votacion.auth_service.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Clase que representa a un usuario en el sistema de votación
// Incluye atributos como nombre, email, contraseña, DNI, código de votación y estado
// Además, maneja roles y permisos asociados al usuario
// Utiliza JPA para la persistencia en base de datos
@Entity
@Table(name = "usuarios")
public class User {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String password; // Solo admins tendrán contraseña

    @Column(unique = true)
    private String dni;

    @Column(length = 12)
    private String votingCode; // Código corto para votantes

    private boolean activo = true;

    private boolean hasVoted = false;

    private int intentosFallidos = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Constructores
    public User() {
    }

    // Constructor para crear un usuario con nombre, email y DNI
    public User(String name, String email, String dni) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.activo = true;
        this.intentosFallidos = 0;
    }

    // Constructor para crear un usuario con nombre, email, contraseña y DNI
    // Este constructor es útil para crear usuarios administradores
    public User(String name, String email, String password, String dni) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        return roles.stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    public String getVotingCode() {
        return votingCode;
    }

    public void setVotingCode(String votingCode) {
        this.votingCode = votingCode;
    }
}
