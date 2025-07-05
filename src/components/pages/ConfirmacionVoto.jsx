import React from 'react';
import { useNavigate } from 'react-router-dom';

const ConfirmacionVoto = () => {
  const navigate = useNavigate();
  const voto = localStorage.getItem('voto');

  const confirmar = () => {
    localStorage.setItem('yaVoto', 'true');
    navigate('/ya-voto');
  };

  return (
    <div>
      <h2>Confirmación del Voto</h2>
      <p>Has seleccionado: <strong>{voto}</strong></p>
      <button onClick={confirmar}>Confirmar voto</button>
    </div>
  );
};

export default ConfirmacionVoto;
