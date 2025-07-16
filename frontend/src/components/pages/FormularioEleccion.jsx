import React, { useState } from 'react';
import Papa from 'papaparse';

const FormularioEleccion = ({ onSubmit, initialData = {} }) => {
  const [titulo, setTitulo] = useState(initialData.titulo || '');
  const [institucion, setInstitucion] = useState(initialData.institucion || '');
  const [fechaInicio, setFechaInicio] = useState(initialData.fechaInicio || '');
  const [fechaCierre, setFechaCierre] = useState(initialData.fechaCierre || '');
  const [categorias, setCategorias] = useState(initialData.categorias || []);
  const [votantes, setVotantes] = useState(initialData.votantes || []);
  const [estado, setEstado] = useState(initialData.estado || 'Borrador');

  const agregarCategoria = () => {
    setCategorias([...categorias, { cargo: '', candidatos: [''] }]);
  };

  const actualizarCategoria = (index, field, value) => {
    const nuevas = [...categorias];
    nuevas[index][field] = value;
    setCategorias(nuevas);
  };

  const actualizarCandidato = (catIndex, candIndex, value) => {
    const nuevas = [...categorias];
    nuevas[catIndex].candidatos[candIndex] = value;
    setCategorias(nuevas);
  };

  const agregarCandidato = (index) => {
    const nuevas = [...categorias];
    nuevas[index].candidatos.push('');
    setCategorias(nuevas);
  };

  const eliminarCandidato = (catIndex, candIndex) => {
    const nuevas = [...categorias];
    nuevas[catIndex].candidatos.splice(candIndex, 1);
    setCategorias(nuevas);
  };

  const handleFileChange = (e) => {
  const file = e.target.files[0];
  if (!file) return;

  Papa.parse(file, {
    header: true,
    skipEmptyLines: true,
    complete: (result) => {
      const datosLimpios = result.data
            .map((v) => ({
          nombre: v.nombre?.trim(),
          dni: v.dni?.trim() || '',
          correo: v.correo?.trim(),
        }))
        .filter((v) => v.nombre && v.dni && v.correo?.includes('@'));
      console.log("📥 Votantes procesados:", datosLimpios);
      setVotantes(datosLimpios);  
    },
  });
};     


  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ titulo, institucion, fechaInicio, fechaCierre, categorias, votantes, estado });
  };

  return (
    <form onSubmit={handleSubmit} className="form-box">
      <h2 className="text-xl font-bold">📋 Datos Generales</h2>
      <input
        type="text"
        placeholder="Título de la elección"
        value={titulo}
        onChange={(e) => setTitulo(e.target.value)}
        required
        className="input-field"
      />
      <input
        type="text"
        placeholder="Institución"
        value={institucion}
        onChange={(e) => setInstitucion(e.target.value)}
        required
        className="input-field"
      />

      <h2 className="text-xl font-bold">📅 Fechas</h2>
      <input
        type="datetime-local"
        value={fechaInicio}
        onChange={(e) => setFechaInicio(e.target.value)}
        required
        className="input-field"
      />
      <input
        type="datetime-local"
        value={fechaCierre}
        onChange={(e) => setFechaCierre(e.target.value)}
        required
        className="input-field"
      />

      <h2 className="text-xl font-bold">🗳️ Cargos y Candidatos</h2>
      {categorias.map((cat, i) => (
        <div key={i} className="border p-4 rounded-md bg-white shadow-sm">
          <input
            type="text"
            placeholder="Nombre del cargo"
            value={cat.cargo}
            onChange={(e) => actualizarCategoria(i, 'cargo', e.target.value)}
            required
            className="input-field mb-2"
          />
          {cat.candidatos.map((cand, j) => (
            <div key={j} className="flex gap-2 items-center mb-2">
              <input
                type="text"
                placeholder={`Candidato ${j + 1}`}
                value={cand}
                onChange={(e) => actualizarCandidato(i, j, e.target.value)}
                required
                className="input-field flex-1"
              />
              {cat.candidatos.length > 1 && (
                <button
                  type="button"
                  onClick={() => eliminarCandidato(i, j)}
                  className="text-red-500 font-bold"
                >
                  ✕
                </button>
              )}
            </div>
          ))}
          <button
            type="button"
            onClick={() => agregarCandidato(i)}
            className="text-blue-600 text-sm"
          >
            + Agregar candidato
          </button>
        </div>
      ))}

      <button type="button" onClick={agregarCategoria} className="text-green-600 text-sm">
        + Agregar cargo
      </button>

      {initialData && Object.keys(initialData).length > 0 && (
        <div>
          <h2 className="text-xl font-bold">⚙️ Estado de la elección</h2>         
          <select value={estado} onChange={(e) => setEstado(e.target.value)} className="input-field">
            <option value="borrador">Borrador</option>
            <option value="activa">Activa</option>
            <option value="cerrada">Cerrada</option>
          </select>

        </div>
      )}

      <h2 className="text-xl font-bold">👥 Votantes Autorizados</h2>
      <input type="file" accept=".csv,.xlsx" onChange={handleFileChange} className="input-field" />
      <p className="text-sm text-gray-500">Archivo con columnas: <strong>nombre</strong>, <strong>correo</strong></p>

      <button type="submit" className="primary-button">
        Guardar elección
      </button>
    </form>
  );
};

export default FormularioEleccion;
