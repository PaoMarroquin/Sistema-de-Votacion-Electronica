import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header style={{ background: '#eee', padding: '1rem' }}>
      <nav style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
        <Link to="/">Inicio</Link>
        <Link to="/login">Iniciar Sesión</Link>
        <Link to="/verificacion">Verificación</Link>
        <Link to="/boleta">Boleta</Link>
        <Link to="/confirmacion">Confirmación</Link>
        <Link to="/ya-voto">Ya Votó</Link>
        <Link to="/admin">Admin</Link>
        <Link to="/admin/elecciones">Elecciones</Link>
        <Link to="/admin/resultados">Resultados</Link>
        <Link to="/admin/auditoria">Auditoría</Link>
      </nav>
    </header>
  );
};

export default Header;
