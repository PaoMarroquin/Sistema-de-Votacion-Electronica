import '../styles/pages.css';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const eleccionesActivas = [
  {
    id: 1,
    institucion: 'Universidad Nacional de Ingeniería',
    fecha: '2025-07-10',
  },
  {
    id: 2,
    institucion: 'Universidad San Marcos',
    fecha: '2025-07-12',
  },
];

const Inicio = () => {
  const navigate = useNavigate();

  const handleParticipar = (id) => {
    // Simula seleccionar una elección
    localStorage.setItem('eleccionActiva', id);
    navigate('/verificacion');
  };

  return (
  <div className="page-container">
    <h2 className="page-title">Elecciones activas</h2>
    <div className="card-grid">
      {eleccionesActivas.map((eleccion) => (
        <div key={eleccion.id} className="card">
          <h3>{eleccion.institucion}</h3>
          <p>📅 Fecha: {eleccion.fecha}</p>
          <button className="primary-button" onClick={() => handleParticipar(eleccion.id)}>
            Participar
          </button>
        </div>
      ))}
    </div>
  </div>
);
};
export default Inicio;
