import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/layout.css';

const Header = () => {
  return (
    <header className="main-header">
      <div className="header-container">
        <h1 className="logo">🗳️ VotoSeguro</h1>
        <nav className="nav-links">
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
      </div>
    </header>
  );
};

export default Header;
