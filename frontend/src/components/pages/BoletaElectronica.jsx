import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const BoletaElectronica = () => {
  const navigate = useNavigate();
  const [selecciones, setSelecciones] = useState({});
  const [categorias, setCategorias] = useState([]);

  useEffect(() => {
    const eleccionGuardada = JSON.parse(localStorage.getItem('eleccionActual'));
    const votante = JSON.parse(localStorage.getItem('votanteActual'));

    if (!eleccionGuardada || !votante) {
      alert('❌ Debes iniciar sesión para votar.');
      navigate('/');
      return;
    }

    if (eleccionGuardada && eleccionGuardada.categorias) {
      setCategorias(eleccionGuardada.categorias);
    } else {
      alert('❌ No se encontró la elección actual.');
      navigate('/');
    }
  }, [navigate]);

  const handleChange = (categoriaId, opcion) => {
    setSelecciones({ ...selecciones, [categoriaId]: opcion });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const faltan = categorias.some((cat) => !selecciones[cat.cargo]);

    if (faltan) {
      alert('⚠️ Debes seleccionar una opción para cada cargo.');
      return;
    }

    localStorage.setItem('selecciones', JSON.stringify(selecciones));
    navigate('/confirmacion');
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Boleta de Votación</h2>
      <form onSubmit={handleSubmit} className="form-box">
        {categorias.map((cat, i) => (
          <div key={i} className="vote-section">
            <h3>{cat.cargo}</h3>
            {cat.candidatos.map((op, j) => (
              <label key={j} className="radio-option">
                <input
                  type="radio"
                  name={cat.cargo}
                  value={op}
                  checked={selecciones[cat.cargo] === op}
                  onChange={() => handleChange(cat.cargo, op)}
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
