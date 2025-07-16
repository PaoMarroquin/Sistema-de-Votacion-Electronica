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
      setError('⚠️ Acceso bloqueado temporalmente por demasiados intentos fallidos.');
      return;
    }

    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];

    let accesoPermitido = false;
    let yaVoto = false;
    let votanteAutenticado = null;
    let eleccionAsociada = null;

    for (const eleccion of elecciones) {
      const votante = eleccion.votantes.find(
        (v) => v.dni === dni && v.token === token
      );

      if (votante) {
        accesoPermitido = true;
        yaVoto = votante.votoEmitido;
        votanteAutenticado = votante;
        eleccionAsociada = eleccion;
        break;
      }
    }

    if (!accesoPermitido) {
      setIntentos(intentos + 1);
      setError('❌ DNI o token incorrectos.');
      return;
    }
    
    localStorage.setItem('votanteActual', JSON.stringify(votanteAutenticado));
    localStorage.setItem('eleccionActual', JSON.stringify(eleccionAsociada));
    localStorage.setItem('dni', dni); 

    if (yaVoto) {
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
