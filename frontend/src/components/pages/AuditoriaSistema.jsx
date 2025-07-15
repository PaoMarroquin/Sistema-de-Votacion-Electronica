import React, { useEffect, useState } from 'react';

const AuditoriaSistema = () => {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    // Simulación de registros, luego esto vendrá del backend o localStorage
    const logsSimulados = [
      {
        id: 1,
        usuario: 'admin1',
        accion: 'Creó una elección',
        fecha: '2025-07-14 10:15:00',
      },
      {
        id: 2,
        usuario: 'admin2',
        accion: 'Editó una elección',
        fecha: '2025-07-14 12:40:00',
      },
      {
        id: 3,
        usuario: 'admin1',
        accion: 'Cerró una elección',
        fecha: '2025-07-15 09:00:00',
      },
    ];

    setLogs(logsSimulados);
  }, []);

  return (
    <div className="page-container">
      <h2 className="page-title">Auditoría del Sistema</h2>
      <p className="fade-text">📋 Acciones registradas recientemente:</p>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '1rem' }}>
          <thead style={{ backgroundColor: 'var(--secondary)' }}>
            <tr>
              <th style={{ padding: '0.8rem', textAlign: 'left' }}>🧑 Usuario</th>
              <th style={{ padding: '0.8rem', textAlign: 'left' }}>⚙️ Acción</th>
              <th style={{ padding: '0.8rem', textAlign: 'left' }}>📅 Fecha</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                <td style={{ padding: '0.8rem' }}>{log.usuario}</td>
                <td style={{ padding: '0.8rem' }}>{log.accion}</td>
                <td style={{ padding: '0.8rem' }}>{log.fecha}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AuditoriaSistema;
