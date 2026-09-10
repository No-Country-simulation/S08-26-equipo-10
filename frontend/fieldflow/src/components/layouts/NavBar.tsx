import { formatCurrentDateTime } from '@/utils/formatters'
import { Bell, ChevronRight } from 'lucide-react'
import { useLocation } from 'react-router'

const pageNames: Record<string, string> = {
    dashboard: 'Dashboard',
    ordenesDeTrabajo: 'Órdenes de Trabajo',
    tecnicos: 'Técnicos',
    clientes: 'Clientes',
    mantenimientos: 'Mantenimientos',
    equipos: 'Equipos',
    agenda: 'Agenda',
}

function Navbar() {
    const location = useLocation()

    const currentPath = location.pathname.split('/').filter(Boolean)
    const currentPage = currentPath[currentPath.length - 1]

    const pageName = pageNames[currentPage] ?? 'FieldFlow'

    const currentDate = new Date().getDate()

    return (
        <header className="flex h-14 items-center justify-between border-b border-slate-800 bg-slate-950 px-4">
            {/* Breadcrumb */}
            <div className="flex items-center gap-2 text-sm">
                <span className="text-slate-500">
                    FieldFlow
                </span>

                <ChevronRight className="h-4 w-4 text-slate-700" />

                <span className="font-semibold text-white">
                    {pageName}
                </span>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-3">
                {/* Notifications */}
                <button
                    type="button"
                    className="relative flex h-8 w-8 items-center justify-center rounded-md text-slate-400 transition hover:bg-slate-900 hover:text-white"
                    aria-label="Notificaciones"
                >
                    <Bell className="h-4 w-4" />

                    <span className="absolute right-1.5 top-1.5 h-1.5 w-1.5 rounded-full bg-red-500" />
                </button>

                {/* System status / date */}
                <div className="flex h-8 items-center gap-2 rounded-md border border-slate-800 bg-slate-900 px-3 text-xs text-slate-400">
                    <span className="h-1.5 w-1.5 rounded-full bg-emerald-500" />

                    <span>
                        {formatCurrentDateTime()}
                    </span>
                </div>
            </div>
        </header>
    )
}

export default Navbar