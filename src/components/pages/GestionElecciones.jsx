import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const GestionElecciones = () => {
  const [elecciones, setElecciones] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const almacenadas = JSON.parse(localStorage.getItem('elecciones')) || [];
    setElecciones(almacenadas);
  }, []);

  const cambiarEstado = (id) => {
    const actualizadas = elecciones.map((eleccion) =>
      eleccion.id === id
        ? {
            ...eleccion,
            estado: eleccion.estado === 'activa' ? 'cerrada' : 'activa',
          }
        : eleccion
    );
    setElecciones(actualizadas);
    localStorage.setItem('elecciones', JSON.stringify(actualizadas)); // ← persistir cambios
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Gestión de Elecciones</h2>
      <button
        className="primary-button"
        style={{ marginBottom: '1rem' }}
        onClick={() => navigate('/admin/elecciones/crear')}
      >
        Crear Nueva Elección
      </button>
      <div className="card-grid">
        {elecciones.map((eleccion) => (
          <div key={eleccion.id} className="card">
            <h3>{eleccion.titulo}</h3> {/* ← asegúrate de que sea "titulo" */}
            <p>
              📅 {eleccion.fechaInicio} a {eleccion.fechaCierre}
            </p>
            <p className={eleccion.estado === 'activa' ? 'success' : 'fade-text'}>
              Estado: {eleccion.estado}
            </p>
            <button
              className="primary-button"
              onClick={() => navigate(`/admin/elecciones/editar/${eleccion.id}`)}
            >
              Editar
            </button>
            <button
              className="primary-button"
              onClick={() => cambiarEstado(eleccion.id)}
              style={{ backgroundColor: eleccion.estado === 'activa' ? '#e11d48' : '#10b981' }}
            >
              {eleccion.estado === 'activa' ? 'Cerrar' : 'Activar'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GestionElecciones;
