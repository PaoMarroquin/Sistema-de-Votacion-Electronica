import React, { useEffect, useState } from 'react';

const ResultadosTiempoReal = () => {
  const [eleccion, setEleccion] = useState(null);
  const [tiempoRestante, setTiempoRestante] = useState('');

  useEffect(() => {
    // Simula carga de datos desde localStorage o backend
    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
    const activa = elecciones.find((e) => e.estado === 'activa');
    if (activa) setEleccion(activa);
  }, []);

  useEffect(() => {
    if (!eleccion) return;

    const interval = setInterval(() => {
      const fin = new Date(eleccion.fechaCierre).getTime();
      const ahora = new Date().getTime();
      const diferencia = fin - ahora;

      if (diferencia <= 0) {
        setTiempoRestante('Finalizado');
        clearInterval(interval);
      } else {
        const horas = Math.floor((diferencia / (1000 * 60 * 60)) % 24);
        const minutos = Math.floor((diferencia / (1000 * 60)) % 60);
        const segundos = Math.floor((diferencia / 1000) % 60);
        setTiempoRestante(`${horas}h ${minutos}m ${segundos}s`);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [eleccion]);

  if (!eleccion) {
    return <p className="fade-text">No hay elecciones activas actualmente.</p>;
  }

  const totalVotantes = eleccion.votantes?.length || 0;
  const votosEmitidos = eleccion.votantes?.filter((v) => v.votoEmitido)?.length || 0;
  const participacion = totalVotantes > 0 ? ((votosEmitidos / totalVotantes) * 100).toFixed(1) : 0;

  return (
    <div className="page-container">
      <h2 className="page-title">Resultados en Tiempo Real</h2>
      <div className="card">
        <h3>{eleccion.titulo}</h3>
        <p>🕒 Tiempo restante: <strong>{tiempoRestante}</strong></p>
        <p>👥 Total de votantes: {totalVotantes}</p>
        <p>✅ Votos emitidos: {votosEmitidos}</p>
        <p className="success">📊 Participación actual: {participacion}%</p>
      </div>
    </div>
  );
};

export default ResultadosTiempoReal;
