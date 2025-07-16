import React, { useEffect, useState } from 'react';

const ResultadosTiempoReal = () => {
  const [eleccionesActivas, setEleccionesActivas] = useState([]);
  const [tiemposRestantes, setTiemposRestantes] = useState({});

  useEffect(() => {
    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
    const activas = elecciones.filter((e) => e.estado === 'activa');
    setEleccionesActivas(activas);
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      const nuevosTiempos = {};

      eleccionesActivas.forEach((eleccion) => {
        const fin = new Date(eleccion.fechaCierre).getTime();
        const ahora = new Date().getTime();
        const diferencia = fin - ahora;

        if (diferencia <= 0) {
          nuevosTiempos[eleccion.id] = 'Finalizado';
        } else {
          const horas = Math.floor((diferencia / (1000 * 60 * 60)) % 24);
          const minutos = Math.floor((diferencia / (1000 * 60)) % 60);
          const segundos = Math.floor((diferencia / 1000) % 60);
          nuevosTiempos[eleccion.id] = `${horas}h ${minutos}m ${segundos}s`;
        }
      });

      setTiemposRestantes(nuevosTiempos);
    }, 1000);

    return () => clearInterval(interval);
  }, [eleccionesActivas]);

  if (eleccionesActivas.length === 0) {
    return <p className="fade-text">No hay elecciones activas actualmente.</p>;
  }

  return (
    <div className="page-container">
      <h2 className="page-title">Resultados en Tiempo Real</h2>
      <div className="card-grid">
      {eleccionesActivas.map((eleccion) => {
        const totalVotantes = eleccion.votantes?.length || 0;
        const votosEmitidos = eleccion.votantes?.filter((v) => v.votoEmitido)?.length || 0;
        const participacion = totalVotantes > 0 ? ((votosEmitidos / totalVotantes) * 100).toFixed(1) : 0;

        return (
          <div key={eleccion.id} className="card">
            <h3>{eleccion.titulo}</h3>
            <p>🕒 Tiempo restante: <strong>{tiemposRestantes[eleccion.id] || 'Cargando...'}</strong></p>
            <p>👥 Total de votantes: {totalVotantes}</p>
            <p>✅ Votos emitidos: {votosEmitidos}</p>
            <p className="success">📊 Participación actual: {participacion}%</p>
          </div>          
        );
      })}
      </div>
    </div>
  );
};

export default ResultadosTiempoReal;
