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
    setError('');

    const usuarioNormalizado = usuario.trim().toLowerCase();

    if (password !== confirmarPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }
    if (password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }    
    const adminsGuardados = JSON.parse(localStorage.getItem('usuariosAdmin')) || [];
    const existe = adminsGuardados.find((u) => u.usuario === usuarioNormalizado);
    if (existe) {
      setError('El nombre de usuario ya está en uso por otro administrador');
      return;
    }
    const nuevoAdmin = {
      nombre: nombre.trim(),
      correo: correo.trim(),
      usuario: usuarioNormalizado,
      password,
      rol: 'admin',
    };
    adminsGuardados.push(nuevoAdmin);
    localStorage.setItem('usuariosAdmin', JSON.stringify(adminsGuardados));
    navigate('/login'); 
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Registro de Administrador</h2>
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
          placeholder="Correo"
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
          Registrar administrador
        </button>

        <p style={{ textAlign: 'center', marginTop: '1rem' }}>
          ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
        </p>
      </form>
    </div>
  );
};

export default Registro;
