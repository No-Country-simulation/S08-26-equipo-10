import { Navigate, Route, Routes } from "react-router"
import MainLayout from "../components/layouts/MainLayout"
import OrdenesTrabajoPage from "../pages/workOrders/WorkOrdersPage"
import Dashboard from "../pages/dashboard/Dashboard"
import Login from "../pages/auth/Login"
import TechniciansPage from "../pages/technicians/TechniciansPage"
import ClientsPage from "../pages/clients/ClientsPage"
import MaintenancesPage from "../pages/maintenance/PreventiveMaintenance"
import NotFound from "../pages/notFound/NotFound"
import EquipmentPage from "@/pages/equipment/Equipment"
import ComponentsCatalog from "@/pages/componentsCatalog/ComponentsCatalog"
import WorkOrderDetailPage from "@/pages/workOrders/WorkOrderDetail"

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
                <Route path="ordenesDeTrabajo/:id" element={<WorkOrderDetailPage />} />
                <Route path="tecnicos" element={<TechniciansPage />} />
                <Route path="clientes" element={<ClientsPage />} />
                <Route path="mantenimientos" element={<MaintenancesPage />} />
                <Route path="equipos" element={<EquipmentPage />} />
                <Route path="componentes" element={<ComponentsCatalog />} />
            </Route>

            <Route path="*" element={<NotFound />} />
        </Route>
    </Routes>)
}

export default AppRoutes
