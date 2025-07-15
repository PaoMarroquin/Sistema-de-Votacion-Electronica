import React from 'react';
import { useNavigate } from 'react-router-dom';

const ConfirmacionVoto = () => {
  const navigate = useNavigate();
  const selecciones = JSON.parse(localStorage.getItem('selecciones')) || {};

  const confirmarVoto = () => {
    localStorage.setItem('yaVoto', 'true');
    navigate('/ya-voto');
  };

return (
  <div className="page-container">
    <h2 className="page-title">Confirmación de Voto</h2>
    <ul className="selection-summary">
      {Object.entries(selecciones).map(([cat, opcion]) => (
        <li key={cat}><strong>{cat}:</strong> {opcion}</li>
      ))}
    </ul>
    <button onClick={confirmarVoto} className="primary-button">Confirmar voto</button>
  </div>
);
};

export default ConfirmacionVoto;
