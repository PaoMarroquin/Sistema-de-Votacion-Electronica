import React from 'react';
import { Link } from 'react-router-dom';

const PanelAdministrador = () => {
  return (
    <div>
      <h2>Panel de Administrador</h2>
      <ul>
        <li><Link to="/admin/elecciones">Gestión de Elecciones</Link></li>
        <li><Link to="/admin/resultados">Resultados en Tiempo Real</Link></li>
        <li><Link to="/admin/auditoria">Auditoría del Sistema</Link></li>
      </ul>
    </div>
  );
};

export default PanelAdministrador;
