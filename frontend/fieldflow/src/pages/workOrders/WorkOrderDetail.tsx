import { ArrowLeft, Pencil } from "lucide-react";
import { useNavigate, useParams } from "react-router";

import { Button } from "@/components/common/Button";
import { StatusBadge } from "@/components/ui/StatusBadge";
import { PriorityBadge } from "@/components/ui/PriorityBadge";

import { DetailCard } from "@/components/ui/DetailCard";
import { DetailRow } from "@/components/ui/DetailRow";

export default function OrdenTrabajoDetailPage() {
    const navigate = useNavigate();
    const { id } = useParams();

    // Temporal mientras conectamos el endpoint de detalle
    const workOrder = {
        id: id ?? "OT-2025-0041",
        status: "IN_PROGRESS" as const,
        priority: "CRITICAL" as const,
        type: "Correctivo",

        description:
            "Chiller presenta fuga de refrigerante R-134a. Se requiere verificación del circuito primario y recarga de gas.",

        scheduledAt: "2025-01-04 08:00",
        estimatedDuration: "4h",
        createdAt: "2025-01-03",
        updatedAt: "2025-01-04",

        client: {
            company: "Metalúrgica del Norte S.A.",
            ruc: "20501234567",
            sector: "Industrial",
            contact: "Ing. Jorge Paredes",
            phone: "+51 944 112 233",
            email: "mantenimiento@metalnorte.pe",
        },

        site: {
            name: "Planta Principal",
            address: "Av. Industrial 2450",
            city: "Lima",
            contact: "César Villanueva",
            phone: "+51 944 223 344",
        },

        equipment: {
            name: "Chiller Industrial Carrier",
            model: "Carrier 30XA-200",
            serialNumber: "CAR-2020-01122",
        },

        technician: {
            name: "Carlos Mendoza Ruiz",
            code: "TEC-001",
            specialty: "HVAC & Refrigeración",
        },
    };

    return (
        <section className="min-h-full bg-[#0b1015] p-6 text-white">

            {/* Header */}
            <div className="mb-6 flex items-start justify-between gap-6">

                <div className="min-w-0">

                    <button
                        type="button"
                        onClick={() => navigate("/fieldflow/ordenesDeTrabajo")}
                        className="mb-3 flex items-center gap-2 text-sm text-slate-400 transition hover:text-white"
                    >
                        <ArrowLeft className="h-4 w-4" />
                        Órdenes de Trabajo
                    </button>

                    <div className="flex flex-wrap items-center gap-3">

                        <h1 className="font-mono text-2xl font-bold text-white">
                            {workOrder.id}
                        </h1>

                        <StatusBadge status={workOrder.status} />

                        <PriorityBadge
                            priority={workOrder.priority}
                        />

                        <span className="font-semibold text-red-400">
                            {workOrder.type}
                        </span>

                    </div>

                    <p className="mt-2 max-w-5xl text-base text-slate-400">
                        {workOrder.description}
                    </p>

                </div>

                <div className="flex shrink-0 gap-3">

                    <Button icon={Pencil}>
                        Editar
                    </Button>

                    <Button>
                        Actualizar Estado
                    </Button>

                </div>
            </div>


            {/* Summary */}
            <div className="mb-6 grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">

                <SummaryCard
                    label="FECHA"
                    value={workOrder.scheduledAt}
                />

                <SummaryCard
                    label="DURACIÓN EST."
                    value={workOrder.estimatedDuration}
                />

                <SummaryCard
                    label="CREADA"
                    value={workOrder.createdAt}
                />

                <SummaryCard
                    label="ACTUALIZADA"
                    value={workOrder.updatedAt}
                />

            </div>


            {/* Tabs */}
            <div className="mb-6 overflow-x-auto border-b border-slate-700">

                <div className="flex min-w-max gap-8">

                    <Tab active>
                        Información
                    </Tab>

                    <Tab>
                        Checklist
                    </Tab>

                    <Tab>
                        Notas Técnicas
                    </Tab>

                    <Tab>
                        Mediciones
                    </Tab>

                    <Tab>
                        Conformidad
                    </Tab>

                    <Tab>
                        Historial
                    </Tab>

                </div>

            </div>


            {/* Information */}
            <div className="grid grid-cols-1 gap-6 xl:grid-cols-2">

                <DetailCard title="Cliente">

                    <DetailRow
                        label="Empresa"
                        value={workOrder.client.company}
                    />

                    <DetailRow
                        label="RUC"
                        value={workOrder.client.ruc}
                    />

                    <DetailRow
                        label="Sector"
                        value={workOrder.client.sector}
                    />

                    <DetailRow
                        label="Contacto"
                        value={workOrder.client.contact}
                    />

                    <DetailRow
                        label="Teléfono"
                        value={workOrder.client.phone}
                    />

                    <DetailRow
                        label="Email"
                        value={workOrder.client.email}
                    />

                </DetailCard>


                <DetailCard title="Sede">

                    <DetailRow
                        label="Nombre"
                        value={workOrder.site.name}
                    />

                    <DetailRow
                        label="Dirección"
                        value={workOrder.site.address}
                    />

                    <DetailRow
                        label="Ciudad"
                        value={workOrder.site.city}
                    />

                    <DetailRow
                        label="Contacto"
                        value={workOrder.site.contact}
                    />

                    <DetailRow
                        label="Teléfono"
                        value={workOrder.site.phone}
                    />

                </DetailCard>


                <DetailCard title="Equipo">

                    <DetailRow
                        label="Nombre"
                        value={workOrder.equipment.name}
                    />

                    <DetailRow
                        label="Marca / Modelo"
                        value={workOrder.equipment.model}
                    />

                    <DetailRow
                        label="N° Serie"
                        value={workOrder.equipment.serialNumber}
                    />

                </DetailCard>


                <DetailCard title="Técnico Asignado">

                    <DetailRow
                        label="Nombre"
                        value={workOrder.technician.name}
                    />

                    <DetailRow
                        label="Código"
                        value={workOrder.technician.code}
                    />

                    <DetailRow
                        label="Especialidad"
                        value={workOrder.technician.specialty}
                    />

                </DetailCard>

            </div>

        </section>
    );
}

interface SummaryCardProps {
    label: string;
    value: string;
}

function SummaryCard({
    label,
    value,
}: SummaryCardProps) {
    return (
        <div className="rounded-lg border border-slate-700 bg-slate-900/70 p-4">
            <p className="text-sm text-slate-400">
                {label}
            </p>

            <p className="mt-1 font-mono text-lg font-semibold text-white">
                {value}
            </p>
        </div>
    );
}

interface TabProps {
    children: React.ReactNode;
    active?: boolean;
}

function Tab({
    children,
    active = false,
}: TabProps) {
    return (
        <button
            type="button"
            className={`border-b-2 px-1 pb-4 pt-2 text-sm font-medium transition ${active
                ? "border-blue-500 text-white"
                : "border-transparent text-slate-400 hover:text-white"
                }`}
        >
            {children}
        </button>
    );
}