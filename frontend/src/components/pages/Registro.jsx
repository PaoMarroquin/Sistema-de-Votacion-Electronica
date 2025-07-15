import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Alerta from '../alerts/Alerta';

const Registro = () => {
  const [nombre, setNombre] = useState('');
  const [correo, setCorreo] = useState('');
  const [usuario, setUsuario] = useState('');
  const [password, setPassword] = useState('');
  const [confirmarPassword, setConfirmarPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    if (password !== confirmarPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    if (password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }
    // Obtener usuarios existentes del localStorage
    const usuariosGuardados = JSON.parse(localStorage.getItem('usuariosMock')) || [];
    // Verificar si el nombre de usuario ya existe
    const existe = usuariosGuardados.find((u) => u.usuario === usuario);
    if (existe) {
      setError('El nombre de usuario ya está en uso');
      return;
    }
    // Agregar nuevo usuario
    const nuevoUsuario = {
      nombre,
      correo,
      usuario,
      password,
    };
    usuariosGuardados.push(nuevoUsuario);
    localStorage.setItem('usuariosMock', JSON.stringify(usuariosGuardados));
    // Redirigir al login
    navigate('/login');
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Registro de Usuario</h2>
      {error && <Alerta mensaje={error} />}
      <form onSubmit={handleSubmit} className="form-box">
        <input
          type="text"
          placeholder="Nombre completo"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
          className="input-field"
        />
        <input
          type="email"
          placeholder="Correo institucional"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
          required
          className="input-field"
        />
        <input
          type="text"
          placeholder="Nombre de usuario"
          value={usuario}
          onChange={(e) => setUsuario(e.target.value)}
          required
          className="input-field"
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className="input-field"
        />
        <input
          type="password"
          placeholder="Confirmar contraseña"
          value={confirmarPassword}
          onChange={(e) => setConfirmarPassword(e.target.value)}
          required
          className="input-field"
        />
        <button type="submit" className="primary-button">
          Registrarse
        </button>

        <p style={{ textAlign: 'center', marginTop: '1rem' }}>
          ¿Ya tienes una cuenta? <Link to="/login">Inicia sesión</Link>
        </p>
      </form>
    </div>
  );
};

export default Registro;
