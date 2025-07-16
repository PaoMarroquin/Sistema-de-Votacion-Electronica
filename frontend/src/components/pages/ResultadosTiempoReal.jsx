import React, { useEffect, useState } from 'react';

const ResultadosTiempoReal = () => {
  const [elecciones, setElecciones] = useState([]);
  const [tiemposRestantes, setTiemposRestantes] = useState({});

  useEffect(() => {
    const interval = setInterval(() => {
      const data = JSON.parse(localStorage.getItem('elecciones')) || [];
      setElecciones(data);

      const nuevosTiempos = {};
      data.forEach((eleccion) => {
        if (eleccion.estado === 'activa') {
          const fin = new Date(eleccion.fechaCierre).getTime();
          const ahora = Date.now();
          const diferencia = fin - ahora;

          nuevosTiempos[eleccion.id] =
            diferencia <= 0
              ? 'Finalizado'
              : `${Math.floor((diferencia / (1000 * 60 * 60)) % 24)}h ${Math.floor((diferencia / (1000 * 60)) % 60)}m ${Math.floor((diferencia / 1000) % 60)}s`;
        } else {
          nuevosTiempos[eleccion.id] = 'Finalizado';
        }
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

  if (elecciones.length === 0) {
    return <p className="fade-text">No hay elecciones registradas.</p>;
  }

  return (
    <div className="page-container">
      <h2 className="page-title">Resultados en Tiempo Real</h2>
      <div className="card-grid">
        {elecciones.map((eleccion) => {
          const totalVotantes = eleccion.votantes?.length || 0;
          const votosEmitidos = eleccion.votantes?.filter((v) => v.votoEmitido)?.length || 0;
          const participacion = totalVotantes > 0 ? ((votosEmitidos / totalVotantes) * 100).toFixed(1) : 0;
          const resultados = contarVotosPorCategoria(eleccion.votantes, eleccion.categorias || []);

          return (
            <div key={eleccion.id} className="card">
              <h3>{eleccion.titulo}</h3>
              <p>
                🕒 Estado:{" "}
                <strong>{eleccion.estado === 'activa' ? 'En curso' : 'Cerrada'}</strong>
              </p>
              <p>🕐 Tiempo restante: <strong>{tiemposRestantes[eleccion.id] || 'Finalizado'}</strong></p>
              <p>👥 Total de votantes: {totalVotantes}</p>
              <p>✅ Votos emitidos: {votosEmitidos}</p>
              <p className="success">📊 Participación: {participacion}%</p>

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
