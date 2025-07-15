import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/pages.css'; 

const PanelAdministrador = () => {
  const opciones = [
    {
      to: "/admin/elecciones",
      icon: "🗳️",
      titulo: "Gestión de Elecciones",
      descripcion: "Crear y administrar elecciones."
    },
    {
      to: "/admin/resultados",
      icon: "📊",
      titulo: "Resultados en Tiempo Real",
      descripcion: "Monitorea los votos al instante."
    },
    {
      to: "/admin/auditoria",
      icon: "📁",
      titulo: "Auditoría del Sistema",
      descripcion: "Revisa los registros de actividad del sistema."
    }
  ];

  return (
    <div className="page-container">
      <h2 className="page-title">Panel de Administrador</h2>
      <div className="card-grid">
        {opciones.map((opcion, index) => (
          <Link to={opcion.to} key={index} className="card" style={{ textDecoration: 'none', color: 'inherit' }}>
            <div className="card-icon" style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>{opcion.icon}</div>
            <h3>{opcion.titulo}</h3>
            <p className="fade-text">{opcion.descripcion}</p>
          </Link>
        ))}
      </div>
    </div>
  );
};

export default PanelAdministrador;
