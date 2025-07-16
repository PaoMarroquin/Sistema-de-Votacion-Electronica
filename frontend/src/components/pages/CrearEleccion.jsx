import React from 'react';
import { useNavigate } from 'react-router-dom';
import FormularioEleccion from './FormularioEleccion';

const CrearEleccion = () => {
  const navigate = useNavigate();

  const handleSubmit = (formData) => {
    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];

    const nuevaEleccion = {
      ...formData,
      id: Date.now().toString(),
      estado: formData.estado || 'borrador',
    };
    elecciones.push(nuevaEleccion);
    localStorage.setItem('elecciones', JSON.stringify(elecciones));
    alert('✅ Elección creada exitosamente');
    navigate('/admin/elecciones');
  };

  return (
    <div className="page-container">
      <h2 className="page-title">Crear Nueva Elección</h2>
      <FormularioEleccion onSubmit={handleSubmit} />
    </div>
  );
};

export default CrearEleccion;
