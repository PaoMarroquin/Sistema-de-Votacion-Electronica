import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const YaVoto = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setTimeout(() => {
      navigate('/');
    }, 4000);
    return () => clearTimeout(timer);
  }, [navigate]);

return (
  <div className="page-container">
    <h2 className="page-title success">✅ Voto registrado exitosamente</h2>
    <p>Gracias por participar.</p>
    <p className="fade-text">Redirigiendo a inicio...</p>
  </div>
);
};

export default YaVoto;
