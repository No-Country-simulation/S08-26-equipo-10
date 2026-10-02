import { Navigate, Route, Routes } from "react-router"
import MainLayout from "../components/layouts/MainLayout"
import OrdenesTrabajoPage from "../features/workOrders/pages/WorkOrdersPage"
import Dashboard from "@/features/dashboard/pages/Dashboard"
import Login from "../features/auth/Login"
import TechniciansPage from "@/features/technicians/pages/TechniciansPage"
import ClientsPage from "../features/clients/pages/ClientsPage"
import MaintenancesPage from "@/features/maintenance/pages/MaintenancePage"
import NotFound from "../features/notFound/NotFound"
import EquipmentPage from "@/features/equipment/pages/EquipmentPage"
import ComponentsCatalog from "@/features/componentsCatalog/ComponentsCatalog"
import WorkOrderDetailPage from "@/features/workOrders/pages/WorkOrderDetail"
import AddWorkOrderPage from "@/features/workOrders/pages/AddWorkOrderPage"
import AssignmentPage from "@/features/assignment/pages/AssignmentPage"

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
                <Route path="ordenesDeTrabajo/nuevaOrden" element={<AddWorkOrderPage />} />
                <Route path="tecnicos" element={<TechniciansPage />} />
                <Route path="clientes" element={<ClientsPage />} />
                <Route path="mantenimientos" element={<MaintenancesPage />} />
                <Route path="equipos" element={<EquipmentPage />} />
                <Route path="asignaciones" element={<AssignmentPage />} />
                <Route path="componentes" element={<ComponentsCatalog />} />
            </Route>

            <Route path="*" element={<NotFound />} />
        </Route>
    </Routes>)
}

export default AppRoutes
