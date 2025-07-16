import '../styles/pages.css';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Inicio = () => {
  const [eleccionesActivas, setEleccionesActivas] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
    const activas = elecciones.filter((e) => e.estado === 'activa'); // ← solo activas
    setEleccionesActivas(activas);
  }, []);

  const handleParticipar = (id) => {
    localStorage.setItem('eleccionActiva', id);
    navigate('/verificacion'); // ← o la ruta que corresponda
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Elecciones activas</h2>
      {eleccionesActivas.length === 0 ? (
        <p className="fade-text">No hay elecciones activas disponibles en este momento.</p>
      ) : (
        <div className="card-grid">
          {eleccionesActivas.map((eleccion) => (
            <div key={eleccion.id} className="card">
              <h3>{eleccion.titulo}</h3>
              <p>🏫 Institución: {eleccion.institucion}</p>
              <p>📅 Fecha: {new Date(eleccion.fechaInicio).toLocaleDateString()}</p>
              <button className="primary-button" onClick={() => handleParticipar(eleccion.id)}>
                Participar
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Inicio;
