import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import FormularioEleccion from './FormularioEleccion';

const EditarEleccion = () => {
  const { id } = useParams(); // el id de la elección viene desde la URL
  const navigate = useNavigate();
  const [eleccion, setEleccion] = useState(null);

  useEffect(() => {
    const eleccionesGuardadas = JSON.parse(localStorage.getItem('elecciones')) || [];
    const encontrada = eleccionesGuardadas.find((e) => e.id === id);
    if (encontrada) {
      setEleccion(encontrada);
    } else {
      alert('❌ Elección no encontrada');
      navigate('/admin/elecciones');
    }
  }, [id, navigate]);

  const handleSubmit = (formData) => {
    const eleccionesGuardadas = JSON.parse(localStorage.getItem('elecciones')) || [];
    const actualizadas = eleccionesGuardadas.map((e) =>
      e.id === id ? { ...formData, id } : e
    );
    localStorage.setItem('elecciones', JSON.stringify(actualizadas));
    alert('✅ Elección actualizada');
    navigate('/admin/elecciones');
  };

  if (!eleccion) return <p>Cargando elección...</p>;

  return (
    <div className="page-container">
      <h2 className="page-title">Editar Elección</h2>
      <FormularioEleccion onSubmit={handleSubmit} initialData={eleccion} />
    </div>
  );
};

export default EditarEleccion;
