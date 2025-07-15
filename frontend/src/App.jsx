import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/layout/Header';
import Footer from './components/layout/Footer';

// Páginas
import Inicio from './components/pages/Inicio';
import IniciarSesion from './components/pages/IniciarSesion';
import VerificacionIdentidad from './components/pages/VerificacionIdentidad';
import BoletaElectronica from './components/pages/BoletaElectronica';
import ConfirmacionVoto from './components/pages/ConfirmacionVoto';
import YaVoto from './components/pages/YaVoto';
import PanelAdministrador from './components/pages/PanelAdministrador';
import GestionElecciones from './components/pages/GestionElecciones';
import ResultadosTiempoReal from './components/pages/ResultadosTiempoReal';
import AuditoriaSistema from './components/pages/AuditoriaSistema';
import Registro from './components/pages/Registro';
import CrearEleccion from './components/pages/CrearEleccion';
import EditarEleccion from './components/pages/EditarEleccion';

function App() {
  return (
    <Router>
      <Header />
      <main style={{ padding: '2rem' }}>
        <Routes>
          <Route path="/" element={<Inicio />} />
          <Route path="/login" element={<IniciarSesion />} />
          <Route path="/registro" element={<Registro />} />
          <Route path="/verificacion" element={<VerificacionIdentidad />} />
          <Route path="/boleta" element={<BoletaElectronica />} />
          <Route path="/confirmacion" element={<ConfirmacionVoto />} />
          <Route path="/ya-voto" element={<YaVoto />} />
          <Route path="/admin" element={<PanelAdministrador />} />
          <Route path="/admin/elecciones" element={<GestionElecciones />} />
          <Route path="/admin/elecciones/crear" element={<CrearEleccion />} />
          <Route path="/admin/elecciones/editar/:id" element={<EditarEleccion />} />
          <Route path="/admin/resultados" element={<ResultadosTiempoReal />} />
          <Route path="/admin/auditoria" element={<AuditoriaSistema />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
}

export default App;
