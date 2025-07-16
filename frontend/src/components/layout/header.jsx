import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/layout.css';

const Header = () => {
  const adminLogueado = JSON.parse(localStorage.getItem('adminLogueado'));
  const navigate = useNavigate();

  const cerrarSesion = () => {
    localStorage.removeItem('adminLogueado');
    navigate('/');
  };

  return (
    <header className="main-header">
      <div className="header-container">
        <h1 className="logo">🗳️ VotoSeguro</h1>

        <nav className="nav-links">         

          {!adminLogueado ? (
            <Link to="/login">Iniciar Sesión Admin</Link>
          ) : (
            <>              
              <button onClick={cerrarSesion} className="logout-button">Cerrar sesión</button>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
