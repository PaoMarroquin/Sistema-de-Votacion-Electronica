import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import FormularioEleccion from './FormularioEleccion';
import emailjs from '@emailjs/browser';

const EditarEleccion = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [eleccion, setEleccion] = useState(null);
  const [reenviarCorreos, setReenviarCorreos] = useState(false);

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

  const generarToken = () => {
    return Math.random().toString(36).substring(2, 12); // 10 caracteres
  };

  const enviarCorreo = (votante, eleccionTitulo, token) => {
    const templateParams = {
      to_name: votante.nombre,
      to_email: votante.correo,
      token: token,
      titulo: eleccionTitulo,
    };

    emailjs
      .send(
        'service_7nv0qbs',     
        'template_6kdlhkl',   
        templateParams,
        'gCVv0tRciSHRa-Y4A' 
      )
      .then(
        (response) => console.log('Correo enviado', response),
        (error) => console.error('Error enviando correo:', error)
      );
  };

  const handleSubmit = (formData) => {
    const eleccionesGuardadas = JSON.parse(localStorage.getItem('elecciones')) || [];

    const votantesActualizados = formData.votantes.map((v) => {
      const token = v.token || generarToken();
      if (reenviarCorreos) {
        enviarCorreo(v, formData.titulo, token);
      }
      return { ...v, token, votoEmitido: v.votoEmitido || false };
    });

    const nuevaEleccion = {
      ...formData,
      votantes: votantesActualizados,
      id,
    };

    const actualizadas = eleccionesGuardadas.map((e) =>
      e.id === id ? nuevaEleccion : e
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

      <div className="mt-4">
        <label className="flex items-center">
          <input
            type="checkbox"
            checked={reenviarCorreos}
            onChange={() => setReenviarCorreos(!reenviarCorreos)}
            className="mr-2"
          />
          Reenviar correos a los votantes
        </label>
      </div>
    </div>
  );
};

export default EditarEleccion;
