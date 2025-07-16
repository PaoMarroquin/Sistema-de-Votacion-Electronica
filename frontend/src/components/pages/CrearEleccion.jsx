import React from 'react';
import { useNavigate } from 'react-router-dom';
import FormularioEleccion from './FormularioEleccion';
import emailjs from '@emailjs/browser'; 

const CrearEleccion = () => {
  const navigate = useNavigate();

  const generarToken = () => {
    return Math.random().toString(36).substring(2, 12); // 10 caracteres aleatorios
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
    const elecciones = JSON.parse(localStorage.getItem('elecciones')) || [];
    console.log("Datos de votantes recibidos:", formData.votantes);
    const votantesConToken = formData.votantes.map((v) => {
      const token = generarToken();
      enviarCorreo(v, formData.titulo, token); 
      return { ...v, token, votoEmitido: false }; 
    });

    const nuevaEleccion = {
      ...formData,
      id: Date.now().toString(),
      votantes: votantesConToken,
    };

    elecciones.push(nuevaEleccion);
    localStorage.setItem('elecciones', JSON.stringify(elecciones));
    alert('✅ Elección creada y tokens enviados');
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
