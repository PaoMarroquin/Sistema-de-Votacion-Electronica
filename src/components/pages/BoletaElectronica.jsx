import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const BoletaElectronica = () => {
  const navigate = useNavigate();
  const [opcion, setOpcion] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (opcion) {
      localStorage.setItem('voto', opcion); // Simula guardar voto
      navigate('/confirmacion');
    }
  };

  return (
    <div>
      <h2>Boleta Electrónica</h2>
      <form onSubmit={handleSubmit}>
        <label>
          <input
            type="radio"
            value="Candidato A"
            checked={opcion === 'Candidato A'}
            onChange={(e) => setOpcion(e.target.value)}
          />
          Candidato A
        </label><br />
        <label>
          <input
            type="radio"
            value="Candidato B"
            checked={opcion === 'Candidato B'}
            onChange={(e) => setOpcion(e.target.value)}
          />
          Candidato B
        </label><br />
        <button type="submit">Continuar</button>
      </form>
    </div>
  );
};

export default BoletaElectronica;
