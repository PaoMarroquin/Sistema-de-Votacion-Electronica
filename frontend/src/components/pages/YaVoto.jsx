import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const YaVoto = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Verificamos si realmente hay un DNI y un votante que haya votado
    const dni = localStorage.getItem('dni');
    const eleccion = JSON.parse(localStorage.getItem('eleccionActual'));

    const votante = eleccion?.votantes?.find((v) => v.dni === dni);

    if (!votante || !votante.votoEmitido) {
      // Si no hay evidencia de voto, redirigimos al inicio
      navigate('/');
      return;
    }

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
