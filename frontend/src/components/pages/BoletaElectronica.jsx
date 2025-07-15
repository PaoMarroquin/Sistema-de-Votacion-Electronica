import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const categorias = [
  {
    id: 'presidencia',
    nombre: 'Presidente',
    opciones: ['Candidato A', 'Candidato B'],
  },
  {
    id: 'representacion',
    nombre: 'Representante Estudiantil',
    opciones: ['Candidato X', 'Candidato Y'],
  },
];

const BoletaElectronica = () => {
  const navigate = useNavigate();
  const [selecciones, setSelecciones] = useState({});

  const handleChange = (categoriaId, opcion) => {
    setSelecciones({ ...selecciones, [categoriaId]: opcion });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    localStorage.setItem('selecciones', JSON.stringify(selecciones));
    navigate('/confirmacion');
  };

return (
  <div className="page-container">
    <h2 className="page-title">Boleta de Votación</h2>
    <form onSubmit={handleSubmit} className="form-box">
      {categorias.map((cat) => (
        <div key={cat.id} className="vote-section">
          <h3>{cat.nombre}</h3>
          {cat.opciones.map((op) => (
            <label key={op} className="radio-option">
              <input
                type="radio"
                name={cat.id}
                value={op}
                checked={selecciones[cat.id] === op}
                onChange={() => handleChange(cat.id, op)}
              />
              {op}
            </label>
          ))}
        </div>
      ))}
      <button type="submit" className="primary-button">Confirmar</button>
    </form>
  </div>
);
};

export default BoletaElectronica;
