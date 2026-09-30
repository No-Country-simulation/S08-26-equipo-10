import {
    ClipboardList,
    LayoutDashboard,
    Wrench,
    Users,
    Building2,
    CalendarDays,
    Package,
    Settings,
} from "lucide-react";
import { NavLink, useNavigate } from "react-router";
import fieldflowLogo from "@/assets/logo.svg";
import { useRoleStore } from "@/store/useRolStore";

const navigationItems = [
    {
        label: "Dashboard",
        path: "/fieldflow/dashboard",
        icon: LayoutDashboard,
        roles: ["ADMIN"],
    },
    {
        label: "Órdenes de Trabajo",
        path: "/fieldflow/ordenesDeTrabajo",
        icon: ClipboardList,
        roles: ["ADMIN"],
    },
    {
        label: "Asignaciones",
        path: "/fieldflow/asignaciones",
        icon: ClipboardList,
        roles: ["TECHNICIAN"],
    },
    {
        label: "Agenda",
        path: "/fieldflow/agenda",
        icon: CalendarDays,
        roles: ["TECHNICIAN"],
    },
    {
        label: "Técnicos",
        path: "/fieldflow/tecnicos",
        icon: Users,
        roles: ["ADMIN"],
    },
    {
        label: "Clientes",
        path: "/fieldflow/clientes",
        icon: Building2,
        roles: ["ADMIN"],
    },
    {
        label: "Equipos",
        path: "/fieldflow/equipos",
        icon: Package,
        roles: ["ADMIN"],
    },
    {
        label: "Mantenimiento",
        path: "/fieldflow/mantenimientos",
        icon: Wrench,
        roles: ["ADMIN"],
    },
];
function Sidebar() {
    const { role, setRole } = useRoleStore();

    const isAdmin = role === "ADMIN";
    const navigate = useNavigate();
    return (
        <aside className="flex h-screen w-60 flex-col border-r border-slate-800 bg-slate-950 text-slate-300">
            {/* Brand */}
            <div className="flex h-16 items-center gap-3 border-b border-slate-800 px-4">
                <div className="flex h-9 w-9 items-center justify-center rounded-lg">
                    <img
                        src={fieldflowLogo}
                        alt="FieldFlow"
                        className="h-16 w-16"
                    />
                </div>

                <div className="leading-tight">
                    <h1 className="text-sm font-bold text-white">
                        FieldFlow
                    </h1>

                    <span className="text-[9px] font-medium tracking-[0.18em] text-slate-500">
                        GESTIÓN DE CAMPO
                    </span>
                </div>
            </div>

            {/* Role selector */}
            <div className="border-b border-slate-800 px-4 py-3">
                <div className="flex overflow-hidden rounded-md border border-slate-700 bg-slate-900">
                    <button
                        type="button"
                        onClick={() => {
                            setRole("ADMIN");
                            navigate("dashboard")
                        }}
                        className={`flex-1 px-3 py-1.5 text-xs font-medium cursor-pointer transition ${isAdmin
                            ? "bg-blue-600 text-white"
                            : "text-slate-400 hover:text-white"
                            }`}
                    >
                        Admin
                    </button>

                    <button
                        type="button"
                        onClick={() => {
                            setRole("TECHNICIAN");
                            navigate("asignaciones")

                        }}
                        className={`flex-1 px-3 py-1.5 text-xs font-medium cursor-pointer transition ${!isAdmin
                            ? "bg-blue-600 text-white"
                            : "text-slate-400 hover:text-white"
                            }`}
                    >
                        Técnico
                    </button>
                </div>
            </div>

            {/* Navigation */}
            <nav className="flex-1 space-y-1 px-2 py-3">
                {navigationItems
                    .filter((item) => item.roles.includes(role))
                    .map((item) => {
                        const Icon = item.icon;

                        return (
                            <NavLink
                                key={item.label}
                                to={item.path}
                                className={({ isActive }) =>
                                    [
                                        "group flex items-center gap-3 rounded-md px-3 py-2.5 text-sm transition-colors",
                                        isActive
                                            ? "bg-blue-600/25 text-blue-400"
                                            : "text-slate-400 hover:bg-slate-900 hover:text-white",
                                    ].join(" ")
                                }
                            >
                                <Icon className="h-4 w-4 shrink-0" />
                                <span className="flex-1">{item.label}</span>
                            </NavLink>
                        );
                    })}
            </nav>

            {/* Bottom actions */}
            <div className="border-t border-slate-800 p-2">
                <button
                    type="button"
                    className="flex w-full items-center gap-3 rounded-md px-3 py-2.5 text-sm text-slate-400 transition hover:bg-slate-900 hover:text-white"
                >
                    <Settings className="h-4 w-4" />
                    <span>Configuración</span>
                </button>

                {/* User */}
                <div className="mt-2 flex items-center gap-3 border-t border-slate-800 px-2 pt-3">
                    <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-blue-900 text-xs font-semibold text-blue-300">
                        AP
                    </div>

                    <div className="min-w-0 flex-1">
                        <p className="truncate text-xs font-semibold text-white">
                            Ana Pacheco
                        </p>

                        <p className="truncate text-[10px] text-slate-500">
                            Coordinadora · {isAdmin ? "Admin" : "Técnico"}
                        </p>
                    </div>

                    <button
                        type="button"
                        className="text-slate-500 transition hover:text-white"
                        aria-label="Configuración del usuario"
                    >
                        <Settings className="h-3.5 w-3.5" />
                    </button>
                </div>
            </div>
        </aside>
    );
}

export default Sidebar;