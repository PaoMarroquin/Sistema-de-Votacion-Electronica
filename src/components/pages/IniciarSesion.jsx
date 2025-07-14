import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
    if (bloqueado) return;

    if (usuario === 'votante1' && password === '123456') {
      navigate('/verificacion');
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
      <h2 className="page-title">Iniciar Sesión</h2>
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
      </form>
    </div>
  );
};

export default IniciarSesion;
