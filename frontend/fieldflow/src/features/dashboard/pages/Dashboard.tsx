import {
    Check,
    Clock3,
    UserRound,
    Settings,

} from "lucide-react";

const stats = [
    {
        label: "Pendientes",
        value: 3,
        icon: Clock3,
        color: "text-yellow-400",
        border: "border-yellow-500/30",
    },
    {
        label: "Asignadas",
        value: 1,
        icon: UserRound,
        color: "text-blue-400",
        border: "border-blue-500/30",
    },
    {
        label: "En ejecución",
        value: 1,
        icon: Settings,
        color: "text-violet-400",
        border: "border-violet-500/30",
    },
    {
        label: "Finalizadas hoy",
        value: 2,
        icon: Check,
        color: "text-emerald-400",
        border: "border-emerald-500/30",
    },
];

const secondaryStats = [
    {
        label: "Técnicos disponibles",
        value: "3",
        description: "de 6 en total",
        color: "text-emerald-400",
    },
    {
        label: "OT críticas activas",
        value: "2",
        description: "requieren atención urgente",
        color: "text-red-400",
    },
    {
        label: "Manttos. pendientes",
        value: "3",
        description: "vencidos o próximos",
        color: "text-orange-400",
    },
];

const todayOrders = [
    {
        id: "OT-2025-0041",
        priority: "Crítica",
        priorityColor:
            "border-red-500/40 bg-red-500/10 text-red-400",
        client: "Metalúrgica del Norte S.A.",
        description:
            "Chiller presenta fuga de refrigerante R-134a. Se requiere verificación...",
        status: "En ejecución",
        statusColor:
            "border-violet-500/40 bg-violet-500/10 text-violet-300",
        time: "08:00 · 4h",
        technician: "Carlos Mendoza",
    },
    {
        id: "OT-2025-0040",
        priority: "Crítica",
        priorityColor:
            "border-red-500/40 bg-red-500/10 text-red-400",
        client: "Empresa Portuaria del Pacífico",
        description:
            "Grúa pórtico LHM 550 fuera de servicio por falla en sistema hidráulico...",
        status: "En camino",
        statusColor:
            "border-cyan-500/40 bg-cyan-500/10 text-cyan-300",
        time: "07:30 · 8h",
        technician: "Diego Flores",
    },
    {
        id: "OT-2025-0039",
        priority: "Alta",
        priorityColor:
            "border-orange-500/40 bg-orange-500/10 text-orange-400",
        client: "Hipermercado Vidal S.A.C.",
        description:
            "Revisión de cabina y sistema de control. Cliente reporta vibraciones...",
        status: "Asignada",
        statusColor:
            "border-blue-500/40 bg-blue-500/10 text-blue-300",
        time: "10:00 · 3h",
        technician: "Roberto Vásquez",
    },
];

const technicians = [
    {
        initials: "CM",
        name: "Carlos Mendoza",
        specialty: "HVAC & Refrigeración",
        status: "En campo",
        statusColor:
            "border-blue-500/40 bg-blue-500/10 text-blue-300",
    },
    {
        initials: "LQ",
        name: "Laura Quispe",
        specialty: "Electricidad Industrial",
        status: "Disponible",
        statusColor:
            "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",
    },
    {
        initials: "DF",
        name: "Diego Flores",
        specialty: "Mecánica Industrial",
        status: "En campo",
        statusColor:
            "border-blue-500/40 bg-blue-500/10 text-blue-300",
    },
    {
        initials: "AT",
        name: "Ana Torres",
        specialty: "Refrigeración Comercial",
        status: "Disponible",
        statusColor:
            "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",
    },
    {
        initials: "RV",
        name: "Roberto Vásquez",
        specialty: "Elevadores & Escaleras",
        status: "Descanso",
        statusColor:
            "border-yellow-500/40 bg-yellow-500/10 text-yellow-400",
    },
    {
        initials: "MS",
        name: "María Salcedo",
        specialty: "Electromecánica",
        status: "Disponible",
        statusColor:
            "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",
    },
];

const maintenanceAlerts = [
    {
        plan: "MP Ascensor ThyssenKrupp",
        equipment: "ThyssenKrupp Evolution 200",
        frequency: "Mensual",
        last: "2024-12-05",
        next: "2025-01-05",
        status: "Vencido",
        statusColor:
            "border-red-500/40 bg-red-500/10 text-red-400",
    },
    {
        plan: "MP HVAC Trane Sintesis",
        equipment: "Trane Sintesis RTAC-130",
        frequency: "Trimestral",
        last: "2024-09-01",
        next: "2025-01-07",
        status: "Próximo",
        statusColor:
            "border-orange-500/40 bg-orange-500/10 text-orange-400",
    },
    {
        plan: "MP Grúa Pórtico Liebherr LHM 550",
        equipment: "Liebherr LHM 550",
        frequency: "Semestral",
        last: "2024-06-20",
        next: "2024-12-20",
        status: "Vencido",
        statusColor:
            "border-red-500/40 bg-red-500/10 text-red-400",
    },
];

function StatCard({
    label,
    value,
    icon: Icon,
    color,
    border,
}: {
    label: string;
    value: number;
    icon: React.ElementType;
    color: string;
    border: string;
}) {
    return (
        <div
            className={`rounded-lg border ${border} bg-slate-900/70 p-5`}
        >
            <div className="flex items-start justify-between">
                <p className="text-sm font-medium uppercase tracking-wide text-[#78a0d2]">
                    {label}
                </p>

                <Icon className={`h-5 w-5 ${color}`} />
            </div>

            <p className={`mt-4 text-4xl font-semibold ${color}`}>
                {value}
            </p>
        </div>
    );
}

export default function Dashboard() {
    const today = new Date();

    const formattedDate = new Intl.DateTimeFormat("es-ES", {
        weekday: "long",
        day: "numeric",
        month: "long",
        year: "numeric",
    }).format(today);

    return (
        <section className="min-h-full p-6 text-white">
            {/* Header */}
            <div className="mb-6">
                <h1 className="text-2xl font-bold">
                    Dashboard Operaciones
                </h1>

                <p className="mt-1 text-base capitalize text-[#78a0d2]">
                    {formattedDate} — Vista general del día
                </p>
            </div>

            {/* Main stats */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
                {stats.map((stat) => (
                    <StatCard
                        key={stat.label}
                        {...stat}
                    />
                ))}
            </div>

            {/* Secondary stats */}
            <div className="mt-6 grid grid-cols-1 gap-4 lg:grid-cols-3">
                {secondaryStats.map((stat) => (
                    <div
                        key={stat.label}
                        className="rounded-lg border border-slate-700 bg-slate-900/70 p-5"
                    >
                        <p className="text-sm font-medium uppercase tracking-wide text-[#78a0d2]">
                            {stat.label}
                        </p>

                        <p
                            className={`mt-3 text-4xl font-semibold ${stat.color}`}
                        >
                            {stat.value}
                        </p>

                        <p className="mt-1 text-sm text-[#52749f]">
                            {stat.description}
                        </p>
                    </div>
                ))}
            </div>

            {/* Today's work + technicians */}
            <div className="mt-7 grid grid-cols-1 gap-6 xl:grid-cols-[minmax(0,2fr)_minmax(320px,1fr)]">
                {/* Today's work */}
                <div className="overflow-hidden rounded-lg border border-slate-700 bg-slate-900/70">
                    <div className="flex items-center justify-between border-b border-slate-700 px-5 py-4">
                        <h2 className="font-semibold">
                            Trabajos de Hoy
                        </h2>

                        <button className="text-sm text-cyan-400 hover:text-cyan-300">
                            Ver todos →
                        </button>
                    </div>

                    <div>
                        {todayOrders.map((order) => (
                            <div
                                key={order.id}
                                className="flex flex-col gap-4 border-b border-slate-700 px-5 py-4 last:border-b-0 lg:flex-row lg:items-center lg:justify-between"
                            >
                                <div className="min-w-0">
                                    <div className="flex flex-wrap items-center gap-2">
                                        <span className="font-mono text-sm text-cyan-400">
                                            {order.id}
                                        </span>

                                        <span
                                            className={`rounded border px-2 py-1 text-xs font-medium ${order.priorityColor}`}
                                        >
                                            {order.priority}
                                        </span>
                                    </div>

                                    <h3 className="mt-2 font-semibold">
                                        {order.client}
                                    </h3>

                                    <p className="mt-1 truncate text-sm text-[#78a0d2]">
                                        {order.description}
                                    </p>
                                </div>

                                <div className="shrink-0 text-left lg:text-right">
                                    <span
                                        className={`inline-flex rounded border px-3 py-1 text-xs font-medium ${order.statusColor}`}
                                    >
                                        {order.status}
                                    </span>

                                    <p className="mt-2 text-sm text-[#78a0d2]">
                                        {order.time}
                                    </p>

                                    <p className="text-sm text-[#78a0d2]">
                                        {order.technician}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Technicians */}
                <div className="overflow-hidden rounded-lg border border-slate-700 bg-slate-900/70">
                    <div className="flex items-center justify-between border-b border-slate-700 px-5 py-4">
                        <h2 className="font-semibold">
                            Técnicos
                        </h2>

                        <button className="text-sm text-cyan-400 hover:text-cyan-300">
                            Ver todos →
                        </button>
                    </div>

                    <div>
                        {technicians.map((technician) => (
                            <div
                                key={technician.name}
                                className="flex items-center gap-3 border-b border-slate-700 px-5 py-3 last:border-b-0"
                            >
                                <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-blue-900 text-sm font-semibold text-blue-400">
                                    {technician.initials}
                                </div>

                                <div className="min-w-0 flex-1">
                                    <p className="truncate font-semibold">
                                        {technician.name}
                                    </p>

                                    <p className="truncate text-sm text-[#78a0d2]">
                                        {technician.specialty}
                                    </p>
                                </div>

                                <span
                                    className={`shrink-0 rounded border px-3 py-1 text-xs font-medium ${technician.statusColor}`}
                                >
                                    {technician.status}
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* Maintenance */}
            <div className="mt-7 overflow-hidden rounded-lg border border-slate-700 bg-slate-900/70">
                <div className="flex items-center justify-between border-b border-slate-700 px-5 py-4">
                    <h2 className="font-semibold">
                        Alertas de Mantenimiento Preventivo
                    </h2>

                    <button className="text-sm text-cyan-400 hover:text-cyan-300">
                        Gestionar →
                    </button>
                </div>

                <div className="overflow-x-auto">
                    <table className="w-full min-w-225 text-sm">
                        <thead>
                            <tr className="border-b border-slate-700 text-left text-[#78a0d2]">
                                <th className="px-5 py-3 font-medium">
                                    PLAN
                                </th>
                                <th className="px-5 py-3 font-medium">
                                    EQUIPO
                                </th>
                                <th className="px-5 py-3 font-medium">
                                    FRECUENCIA
                                </th>
                                <th className="px-5 py-3 font-medium">
                                    ÚLTIMA VEZ
                                </th>
                                <th className="px-5 py-3 font-medium">
                                    PRÓXIMO
                                </th>
                                <th className="px-5 py-3 font-medium">
                                    ESTADO
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            {maintenanceAlerts.map((alert) => (
                                <tr
                                    key={alert.plan}
                                    className="border-b border-slate-700 last:border-b-0"
                                >
                                    <td className="px-5 py-4 font-medium">
                                        {alert.plan}
                                    </td>

                                    <td className="px-5 py-4 text-[#78a0d2]">
                                        {alert.equipment}
                                    </td>

                                    <td className="px-5 py-4 text-[#78a0d2]">
                                        {alert.frequency}
                                    </td>

                                    <td className="px-5 py-4 font-mono text-[#78a0d2]">
                                        {alert.last}
                                    </td>

                                    <td className="px-5 py-4 font-mono text-[#78a0d2]">
                                        {alert.next}
                                    </td>

                                    <td className="px-5 py-4">
                                        <span
                                            className={`rounded border px-3 py-1 text-xs font-medium ${alert.statusColor}`}
                                        >
                                            {alert.status}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    );
}