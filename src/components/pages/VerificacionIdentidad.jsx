import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Alerta from '../alerts/Alerta';

const VerificacionIdentidad = () => {
  const [dni, setDni] = useState('');
  const [token, setToken] = useState('');
  const [intentos, setIntentos] = useState(0);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleVerificar = (e) => {
    e.preventDefault();

    if (intentos >= 3) {
      setError('Acceso bloqueado temporalmente.');
      return;
    }

    // Simulación de verificación de usuario
    const accesoPermitido = dni === '12345678' && token === 'ABC123';
    const yaVoto = localStorage.getItem('yaVoto');

    if (!accesoPermitido) {
      const nuevosIntentos = intentos + 1;
      setIntentos(nuevosIntentos);
      setError('Datos incorrectos.');
    } else if (yaVoto === 'true') {
      navigate('/ya-voto');
    } else {
      navigate('/boleta');
    }
  };

  return (
  <div className="page-container">
    <h2 className="page-title">Verificación de Identidad</h2>
    {error && <Alerta mensaje={error} />}
    <form onSubmit={handleVerificar} className="form-box">
      <input
        type="text"
        placeholder="DNI"
        value={dni}
        onChange={(e) => setDni(e.target.value)}
        required
        className="input-field"
      />
      <input
        type="text"
        placeholder="Token recibido por correo"
        value={token}
        onChange={(e) => setToken(e.target.value)}
        required
        className="input-field"
      />
      <button type="submit" className="primary-button">Validar</button>
    </form>
  </div>
  );
};

export default VerificacionIdentidad;
