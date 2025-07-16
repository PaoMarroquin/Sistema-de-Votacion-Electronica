import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Alerta from '../alerts/Alerta';

const IniciarSesion = () => {
  const [usuario, setUsuario] = useState('');
  const [password, setPassword] = useState('');
  const [intentos, setIntentos] = useState(0);
  const [bloqueado, setBloqueado] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (bloqueado) return;
    const usuariosAdmin = JSON.parse(localStorage.getItem('usuariosAdmin')) || [];
    const usuarioValido = usuariosAdmin.find(
      (u) => u.usuario === usuario.trim().toLowerCase() && u.password === password
    );
    if (usuarioValido) {
      localStorage.setItem('adminLogueado', JSON.stringify(usuarioValido));
      navigate('/admin');
    } else {
      const nuevosIntentos = intentos + 1;
      setIntentos(nuevosIntentos);
      setError('Credenciales incorrectas');
      if (nuevosIntentos >= 3) {
        setBloqueado(true);
        setError('Cuenta bloqueada por demasiados intentos.');
      }
    }
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Iniciar Sesión (Administrador)</h2>
      {error && <Alerta mensaje={error} />}
      <form onSubmit={handleSubmit} className="form-box">
        <input
          type="text"
          placeholder="Usuario"
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
        <button type="submit" className="primary-button" disabled={bloqueado}>
          Ingresar
        </button>
        <p style={{ textAlign: 'center', marginTop: '1rem' }}>
          ¿No tienes cuenta? <Link to="/registro">Regístrate aquí</Link>
        </p>
      </form>
    </div>
  );
};

export default IniciarSesion;
