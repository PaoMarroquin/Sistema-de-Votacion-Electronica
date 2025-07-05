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

    // Simulación de autenticación
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
    <div>
      <h2>Iniciar Sesión</h2>
      {error && <Alerta mensaje={error} />}
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Usuario"
          value={usuario}
          onChange={(e) => setUsuario(e.target.value)}
          required
        /><br />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        /><br />
        <button type="submit" disabled={bloqueado}>Ingresar</button>
      </form>
    </div>
  );
};

export default IniciarSesion;
