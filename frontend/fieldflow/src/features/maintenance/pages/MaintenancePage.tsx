import {
    AlertTriangle,
    CheckCircle2,
    Clock3,
    Plus,
} from "lucide-react";
import { useNavigate } from "react-router";

import { Button } from "@/components/common/Button";
import { createColumn, Table } from "@/components/common/Table";

type MaintenanceStatus = "OVERDUE" | "UPCOMING" | "UP_TO_DATE";

interface MaintenancePlan {
    id: number;
    name: string;
    equipment: string;
    equipmentType: string;
    client: string;
    frequency: string;
    lastExecution: string;
    nextExecution: string;
    status: MaintenanceStatus;
    tasks: string[];
}
const column = createColumn<MaintenancePlan>();

const maintenancePlans: MaintenancePlan[] = [
    {
        id: 1,
        name: "MP Compresor Atlas Copco GA37",
        equipment: "Atlas Copco GA 37+",
        equipmentType: "Compresor",
        client: "Metalúrgica del Norte S.A.",
        frequency: "Semestral",
        lastExecution: "2025-01-02",
        nextExecution: "2025-07-02",
        status: "UP_TO_DATE",
        tasks: [],
    },
    {
        id: 2,
        name: "MP Chiller Carrier 30XA",
        equipment: "Carrier 30XA-200",
        equipmentType: "HVAC",
        client: "Metalúrgica del Norte S.A.",
        frequency: "Semestral",
        lastExecution: "2024-12-01",
        nextExecution: "2025-06-01",
        status: "UP_TO_DATE",
        tasks: [],
    },
    {
        id: 3,
        name: "MP Ascensor ThyssenKrupp",
        equipment: "ThyssenKrupp Evolution 200",
        equipmentType: "Elevación",
        client: "Hipermercado Vidal S.A.C.",
        frequency: "Mensual",
        lastExecution: "2024-12-05",
        nextExecution: "2025-01-05",
        status: "OVERDUE",
        tasks: [
            "Lubricación guías y cables",
            "Ajuste de frenos",
            "Verificación nivelación",
            "Inspección de puertas",
            "Revisión de seguridad",
        ],
    },
    {
        id: 4,
        name: "MP Grupo Electrógeno Cummins",
        equipment: "Cummins C200D5",
        equipmentType: "Energía",
        client: "Hospital San Rafael",
        frequency: "Semestral",
        lastExecution: "2025-01-03",
        nextExecution: "2025-07-03",
        status: "UP_TO_DATE",
        tasks: [],
    },
    {
        id: 5,
        name: "MP HVAC Trane Sintesis",
        equipment: "Trane Sintesis RTAC-130",
        equipmentType: "HVAC",
        client: "Centro Comercial Plaza Mayor",
        frequency: "Trimestral",
        lastExecution: "2024-09-01",
        nextExecution: "2025-01-07",
        status: "UPCOMING",
        tasks: [
            "Limpieza de filtros y serpentines",
            "Revisión y ajuste de correas",
            "Verificación de caudales",
            "Revisión eléctrica",
            "Prueba de funcionamiento",
        ],
    },
    {
        id: 6,
        name: "MP Grúa Pórtico Liebherr LHM 550",
        equipment: "Liebherr LHM 550",
        equipmentType: "Grúa",
        client: "Empresa Portuaria del Pacífico",
        frequency: "Semestral",
        lastExecution: "2024-06-20",
        nextExecution: "2024-12-20",
        status: "OVERDUE",
        tasks: [
            "Inspección estructura metálica",
            "Lubricación sistema de giro",
            "Revisión sistema hidráulico",
            "Inspección de cables",
            "Prueba operacional",
        ],
    },
];

const statusConfig = {
    OVERDUE: {
        label: "Vencido",
        text: "text-red-400",
        badge: "border-red-500/40 bg-red-500/10 text-red-400",
    },
    UPCOMING: {
        label: "Próximo",
        text: "text-orange-400",
        badge: "border-orange-500/40 bg-orange-500/10 text-orange-400",
    },
    UP_TO_DATE: {
        label: "Al día",
        text: "text-emerald-400",
        badge: "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",
    },
};

function MaintenanceCard({
    plan,
    action = false,
}: {
    plan: MaintenancePlan;
    action?: boolean;
}) {
    const status = statusConfig[plan.status];

    return (
        <div
            className={`flex flex-col gap-4 border-b border-slate-700 px-5 py-5 last:border-b-0 lg:flex-row lg:items-center lg:justify-between ${plan.status === "OVERDUE"
                ? "bg-red-950/10"
                : ""
                }`}
        >
            <div className="min-w-0">
                <h3 className="font-semibold text-white">
                    {plan.name}
                </h3>

                <p className="mt-1 text-sm text-[#78a0d2]">
                    {plan.equipment} · {plan.client}
                </p>

                <p className="mt-1 font-mono text-sm text-[#52749f]">
                    Últ.: {plan.lastExecution}
                    {" → "}
                    <span className={status.text}>
                        Próx.: {plan.nextExecution}
                    </span>
                </p>

                {plan.tasks.length > 0 && (
                    <div className="mt-3 flex flex-wrap gap-2">
                        {plan.tasks.slice(0, 3).map((task) => (
                            <span
                                key={task}
                                className="rounded-md border border-slate-700 bg-slate-800 px-2 py-1 text-xs text-[#78a0d2]"
                            >
                                {task}
                            </span>
                        ))}

                        {plan.tasks.length > 3 && (
                            <span className="px-1 py-1 text-xs text-[#52749f]">
                                +{plan.tasks.length - 3} más
                            </span>
                        )}
                    </div>
                )}
            </div>

            <div className="flex shrink-0 items-center gap-3">
                <span className="rounded-md border border-slate-700 bg-slate-800 px-3 py-1 text-sm text-[#78a0d2]">
                    {plan.frequency}
                </span>

                {action && (
                    <Button>
                        Crear OT
                    </Button>
                )}
            </div>
        </div>
    );
}


const columns = [
    column({
        header: "PLAN",
        accessor: "name",
        render: (value) => (
            <span className="font-mono text-xs  text-cyan-400">
                {value}
            </span>
        ),
    }),

    column({
        header: "EQUIPO",
        accessor: "equipment",
        render: (value) => (

            <p className="font-semibold text-white">
                {value ?? "-"}
            </p>
        ),
    }),

    column({
        header: "CLIENTE",
        accessor: "client",
        render: (value) => (

            <p className="font-medium text-white">
                {value}
            </p>
        ),
    }),

    column({
        header: "FRECUENCIA",
        accessor: "frequency",
        render: (value) => (
            <span className="font-semibold text-blue-400">
                {value}
            </span>
        ),
    }),

    column({
        header: "ULTIMA EJECUCION",
        accessor: "lastExecution",
        render: (value) => (
            <div className="flex items-center justify-center gap-2">
                {value}
            </div>
        ),
    }),


    column({
        header: "PROXIMA EJECUCION",
        accessor: "nextExecution",
        render: (value) => (
            <div className="flex items-center justify-center gap-2">
                {value}
            </div>
        ),
    }),

    column({
        header: "ESTADO",
        accessor: "status",
        render: (value) => (
            <div className="font-mono text-xs flex flex-col gap-1 items-center">
                <p className="text-blue-300">
                    {value}
                </p>
            </div>
        ),
    }),

];


export default function MaintenancePage() {
    const navigate = useNavigate();

    const overdue = maintenancePlans.filter(
        (plan) => plan.status === "OVERDUE"
    );

    const upcoming = maintenancePlans.filter(
        (plan) => plan.status === "UPCOMING"
    );

    const upToDate = maintenancePlans.filter(
        (plan) => plan.status === "UP_TO_DATE"
    );


    return (
        <section className="min-h-full p-6 text-white">
            {/* Header */}
            <div className="mb-8 flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-bold">
                        Mantenimiento Preventivo
                    </h1>

                    <p className="mt-1 text-base text-[#78a0d2]">
                        {maintenancePlans.length} planes activos
                    </p>
                </div>

                <Button
                    onClick={() => navigate("nuevoPlan")}
                    icon={Plus}
                >
                    Nuevo Plan
                </Button>
            </div>

            {/* Summary */}
            <div className="grid grid-cols-1 gap-5 lg:grid-cols-3">
                {/* Overdue */}
                <div className="rounded-lg border border-red-500/40 bg-red-950/30 p-5">
                    <div className="flex items-center gap-2">
                        <AlertTriangle className="h-4 w-4 text-red-400" />

                        <p className="text-sm font-medium uppercase tracking-wide text-red-400">
                            Vencidos
                        </p>
                    </div>

                    <p className="mt-3 text-4xl font-semibold text-red-400">
                        {overdue.length}
                    </p>

                    <p className="mt-2 text-sm text-red-400/80">
                        Requieren atención inmediata
                    </p>
                </div>

                {/* Upcoming */}
                <div className="rounded-lg border border-orange-500/40 bg-orange-950/30 p-5">
                    <div className="flex items-center gap-2">
                        <Clock3 className="h-4 w-4 text-orange-400" />

                        <p className="text-sm font-medium uppercase tracking-wide text-orange-400">
                            Próximos
                        </p>
                    </div>

                    <p className="mt-3 text-4xl font-semibold text-orange-400">
                        {upcoming.length}
                    </p>

                    <p className="mt-2 text-sm text-orange-400/80">
                        En los próximos 30 días
                    </p>
                </div>

                {/* Up to date */}
                <div className="rounded-lg border border-emerald-500/40 bg-emerald-950/30 p-5">
                    <div className="flex items-center gap-2">
                        <CheckCircle2 className="h-4 w-4 text-emerald-400" />

                        <p className="text-sm font-medium uppercase tracking-wide text-emerald-400">
                            Al día
                        </p>
                    </div>

                    <p className="mt-3 text-4xl font-semibold text-emerald-400">
                        {upToDate.length}
                    </p>

                    <p className="mt-2 text-sm text-emerald-400/80">
                        Sin alertas pendientes
                    </p>
                </div>
            </div>

            {/* Overdue */}
            <div className="mt-7 overflow-hidden rounded-lg border border-red-500/40 bg-red-950/10">
                <div className="flex items-center gap-2 border-b border-red-500/30 px-5 py-4">
                    <AlertTriangle className="h-5 w-5 text-red-400" />

                    <h2 className="font-semibold text-red-300">
                        Mantenimientos Vencidos
                    </h2>
                </div>

                {overdue.length > 0 ? (
                    overdue.map((plan) => (
                        <MaintenanceCard
                            key={plan.id}
                            plan={plan}
                            action
                        />
                    ))
                ) : (
                    <div className="px-5 py-8 text-center text-[#78a0d2]">
                        No hay mantenimientos vencidos
                    </div>
                )}
            </div>

            {/* Upcoming */}
            <div className="mt-7 overflow-hidden rounded-lg border border-orange-500/40 bg-slate-900/70">
                <div className="flex items-center gap-2 border-b border-slate-700 px-5 py-4">
                    <Clock3 className="h-5 w-5 text-orange-400" />

                    <h2 className="font-semibold">
                        Próximos Mantenimientos
                    </h2>
                </div>

                {upcoming.length > 0 ? (
                    upcoming.map((plan) => (
                        <MaintenanceCard
                            key={plan.id}
                            plan={plan}
                            action
                        />
                    ))
                ) : (
                    <div className="px-5 py-8 text-center text-[#78a0d2]">
                        No hay mantenimientos próximos
                    </div>
                )}
            </div>

            {/* All plans */}
            <div className="mt-7">
                <Table
                    columns={columns}
                    data={maintenancePlans}
                    isLoading={false}
                    isError={false}
                    errorMessage="Ocurrió un error al cargar los planes de mantenimiento"
                    emptyMessage="No hay planes de mantenimiento para mostrar"
                />
            </div>
        </section>
    );
}