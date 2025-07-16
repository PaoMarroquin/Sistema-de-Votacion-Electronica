import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ConfirmacionVoto = () => {
  const navigate = useNavigate();
  const selecciones = JSON.parse(localStorage.getItem('selecciones')) || {};
  const dniActual = localStorage.getItem('dni');
  const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
  const eleccionActual = JSON.parse(localStorage.getItem('eleccionActual'));

  useEffect(() => {
    if (!eleccionActual || !dniActual || Object.keys(selecciones).length === 0) {
      alert('❌ Datos incompletos. Redirigiendo al inicio.');
      navigate('/');
    }
  }, [navigate, eleccionActual, dniActual, selecciones]);

  const confirmarVoto = () => {
    const eleccionesActualizadas = elecciones.map((e) => {
      if (e.id === eleccionActual.id) {
        const nuevosVotantes = e.votantes.map((v) =>
          v.dni === dniActual
            ? {
                ...v,
                votoEmitido: true,
                voto: selecciones
              }
            : v
        );
        return { ...e, votantes: nuevosVotantes };
      }
      return e;
    });

    // Guardar elecciones actualizadas
    localStorage.setItem('elecciones', JSON.stringify(eleccionesActualizadas));

    // 🔄 Actualizar también la versión actual de la elección
    const nuevaEleccionActualizada = eleccionesActualizadas.find(e => e.id === eleccionActual.id);
    localStorage.setItem('eleccionActual', JSON.stringify(nuevaEleccionActualizada));

    // Limpiar selecciones del localStorage si deseas
    localStorage.removeItem('selecciones');

    navigate('/ya-voto');
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Confirmación de Voto</h2>
      <ul className="selection-summary">
        {Object.entries(selecciones).map(([cat, opcion]) => (
          <li key={cat}>
            <strong>{cat}:</strong> {opcion}
          </li>
        ))}
      </ul>
      <button onClick={confirmarVoto} className="primary-button">
        Confirmar voto
      </button>
    </div>
  );
};

export default ConfirmacionVoto;
