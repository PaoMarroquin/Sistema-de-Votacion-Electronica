import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const VerificacionIdentidad = () => {
  const [dni, setDni] = useState('');
  const navigate = useNavigate();

  const handleVerificar = (e) => {
    e.preventDefault();
    // Suponemos que el DNI existe
    if (dni.length === 8) {
      navigate('/boleta');
    } else {
      alert('DNI inválido');
    }
  };

  return (
    <div>
      <h2>Verificación de Identidad</h2>
      <form onSubmit={handleVerificar}>
        <input
          type="text"
          placeholder="DNI"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          required
        /><br />
        <button type="submit">Verificar</button>
      </form>
    </div>
  );
};

export default VerificacionIdentidad;
