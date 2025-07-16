import React, { useEffect, useState } from 'react';

const ResultadosTiempoReal = () => {
  const [eleccionesActivas, setEleccionesActivas] = useState([]);
  const [tiemposRestantes, setTiemposRestantes] = useState({});

  useEffect(() => {
    const interval = setInterval(() => {
      const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
      const activas = elecciones.filter((e) => e.estado === 'activa');
      setEleccionesActivas(activas);

      const nuevosTiempos = {};
      activas.forEach((eleccion) => {
        const fin = new Date(eleccion.fechaCierre).getTime();
        const ahora = Date.now();
        const diferencia = fin - ahora;

        nuevosTiempos[eleccion.id] = diferencia <= 0
          ? 'Finalizado'
          : `${Math.floor((diferencia / (1000 * 60 * 60)) % 24)}h ${Math.floor((diferencia / (1000 * 60)) % 60)}m ${Math.floor((diferencia / 1000) % 60)}s`;
      });

      setTiemposRestantes(nuevosTiempos);
    }, 2000); 

    return () => clearInterval(interval);
  }, []);

  const contarVotosPorCategoria = (votantes, categorias) => {
    const resultados = {};

    categorias.forEach((cat) => {
      resultados[cat.cargo] = {};
      cat.candidatos.forEach((candidato) => {
        resultados[cat.cargo][candidato] = 0;
      });
    });

    votantes.forEach((v) => {
      if (v.votoEmitido && v.voto) {
        Object.entries(v.voto).forEach(([cargo, candidato]) => {
          if (resultados[cargo] && resultados[cargo][candidato] !== undefined) {
            resultados[cargo][candidato]++;
          }
        });
      }
    });

    return resultados;
  };

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
          const resultados = contarVotosPorCategoria(eleccion.votantes, eleccion.categorias || []);

          return (
            <div key={eleccion.id} className="card">
              <h3>{eleccion.titulo}</h3>
              <p>🕒 Tiempo restante: <strong>{tiemposRestantes[eleccion.id] || 'Cargando...'}</strong></p>
              <p>👥 Total de votantes: {totalVotantes}</p>
              <p>✅ Votos emitidos: {votosEmitidos}</p>
              <p className="success">📊 Participación actual: {participacion}%</p>

              {Object.entries(resultados).map(([cargo, votos]) => (
                <div key={cargo} className="result-box">
                  <h4>{cargo}</h4>
                  <ul>
                    {Object.entries(votos).map(([candidato, cantidad]) => (
                      <li key={candidato}>{candidato}: {cantidad} voto(s)</li>
                    ))}
                  </ul>
                </div>
              ))}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ResultadosTiempoReal;
