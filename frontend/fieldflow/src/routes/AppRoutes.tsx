import { Navigate, Route, Routes } from "react-router"
import MainLayout from "../components/layouts/MainLayout"
import OrdenesTrabajoPage from "../pages/ordenes/OrdenesTrabajoiPage"
import Dashboard from "../pages/dashboard/Dashboard"
import Login from "../pages/auth/Login"
import TecnicosPage from "../pages/tecnicos/TecnicosPage"
import ClientesPage from "../pages/clientes/ClientesPage"
import MantenimientosPage from "../pages/mantenimiento/Preventivos"
import NotFound from "../pages/notFound/NotFound"
import EquiposPage from "@/pages/equipos/Equipos"

function AppRoutes() {
    return (<Routes>
        <Route
            path="/"
            element={<Navigate to="/fieldflow/login" replace />}
        />

        <Route
            path="fieldflow"
            element={<Navigate to="/fieldflow/login" replace />}
        />

        <Route path="fieldflow">
            <Route path="login" element={<Login />} />

            <Route element={<MainLayout />}>
                <Route path="dashboard" element={<Dashboard />} />
                <Route path="ordenesDeTrabajo" element={<OrdenesTrabajoPage />} />
                <Route path="tecnicos" element={<TecnicosPage />} />
                <Route path="clientes" element={<ClientesPage />} />
                <Route path="mantenimientos" element={<MantenimientosPage />} />
                <Route path="equipos" element={<EquiposPage />} />
            </Route>

            <Route path="*" element={<NotFound />} />
        </Route>
    </Routes>)
}

export default AppRoutes
