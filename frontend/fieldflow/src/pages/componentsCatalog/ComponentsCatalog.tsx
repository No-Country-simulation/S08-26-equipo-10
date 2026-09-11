import { useState } from "react";
import { Check, Clock3, Settings, UserRound } from "lucide-react";


import { ErrorMessage } from "@/components/common/ErrorMessage";
import { EmptyState } from "@/components/common/EmptyState";
import { TechnicianStatusBadge } from "@/components/ui/TechnicianStatusBadge";
import { Button } from "@/components/common/Button";
import { Badge } from "@/components/common/Badge";
import { PriorityBadge } from "@/components/ui/PriorityBadge";
import { StatusBadge } from "@/components/ui/StatusBadge";
import { StatCard } from "@/components/ui/StatCard";
import { Input } from "@/components/common/Input";
import { Select } from "@/components/common/Select";
import { Table } from "@/components/common/Table";
import { Loading } from "@/components/common/loading";
import { Modal } from "@/components/common/modal";



interface Technician {
    name: string;
    code: string;
    status: string;
}

const technicians: Technician[] = [
    {
        name: "Carlos Mendoza Ruiz",
        code: "TEC-001",
        status: "En campo",
    },
    {
        name: "Diego Flores",
        code: "TEC-002",
        status: "Disponible",
    },
    {
        name: "Roberto Vásquez",
        code: "TEC-003",
        status: "Ocupado",
    },
];

export default function ComponentsCatalog() {
    const [modalOpen, setModalOpen] = useState(false);

    const technicianColumns = [
        {
            header: "Técnico",
            accessor: "name" as keyof Technician,
        },
        {
            header: "Código",
            accessor: "code" as keyof Technician,
        },
        {
            header: "Estado",
            accessor: "status" as keyof Technician,
            render: (value: Technician["status"]) => (
                <TechnicianStatusBadge
                    status={
                        value as "En campo" | "Disponible" | "Ocupado"
                    }
                />
            ),
        },
    ];

    return (
        <main className="min-h-screen bg-slate-950 p-6 text-white">
            <div className="mx-auto max-w-7xl">

                {/* Header */}
                <header className="mb-8">
                    <p className="text-sm font-medium uppercase tracking-wider text-cyan-400">
                        FieldFlow
                    </p>

                    <h1 className="mt-1 text-3xl font-bold">
                        Catálogo de componentes
                    </h1>

                    <p className="mt-2 text-slate-400">
                        Componentes reutilizables de la aplicación.
                    </p>
                </header>

                {/* BUTTON */}
                <Section title="Button">
                    <div className="flex flex-wrap gap-3">
                        <Button>
                            Crear OT
                        </Button>

                        <Button variant="secondary">
                            Cancelar
                        </Button>

                        <Button variant="danger">
                            Eliminar
                        </Button>

                        <Button variant="ghost">
                            Ver detalles
                        </Button>
                    </div>
                </Section>

                {/* INPUT */}
                <Section title="Input">
                    <div className="grid max-w-2xl gap-4 md:grid-cols-2">
                        <Input
                            label="Nombre"
                            placeholder="Carlos Mendoza"
                        />

                        <Input
                            label="Correo"
                            type="email"
                            placeholder="correo@fieldflow.pe"
                        />

                        <Input
                            label="Input con error"
                            placeholder="Ingrese un valor"
                            error="Este campo es obligatorio"
                        />

                        <Input
                            label="Deshabilitado"
                            placeholder="No disponible"
                            disabled
                        />
                    </div>
                </Section>

                {/* SELECT */}
                <Section title="Select">
                    <div className="max-w-md">
                        <Select
                            label="Prioridad"
                            options={[
                                {
                                    label: "Crítica",
                                    value: "CRITICA",
                                },
                                {
                                    label: "Alta",
                                    value: "ALTA",
                                },
                                {
                                    label: "Media",
                                    value: "MEDIA",
                                },
                            ]}
                        />
                    </div>
                </Section>

                {/* BADGES */}
                <Section title="Badge / Status">
                    <div className="space-y-6">

                        {/* Badge base */}
                        <div>
                            <ComponentLabel>
                                Badge base
                            </ComponentLabel>

                            <div className="flex flex-wrap gap-3">
                                <Badge className="border-slate-600 bg-slate-800 text-slate-300">
                                    General
                                </Badge>

                                <Badge className="border-cyan-500/40 bg-cyan-500/10 text-cyan-400">
                                    Información
                                </Badge>
                            </div>
                        </div>

                        {/* Prioridades */}
                        <div>
                            <ComponentLabel>
                                Prioridades
                            </ComponentLabel>

                            <div className="flex flex-wrap gap-3">
                                <PriorityBadge priority="Crítica" />
                                <PriorityBadge priority="Alta" />
                                <PriorityBadge priority="Media" />
                            </div>
                        </div>

                        {/* Estados OT */}
                        <div>
                            <ComponentLabel>
                                Estados de OT
                            </ComponentLabel>

                            <div className="flex flex-wrap gap-3">
                                <StatusBadge status="En ejecución" />
                                <StatusBadge status="En camino" />
                                <StatusBadge status="Asignada" />
                                <StatusBadge status="Pendiente" />
                                <StatusBadge status="Finalizada" />
                                <StatusBadge status="Reprogramada" />
                            </div>
                        </div>

                        {/* Estados técnicos */}
                        <div>
                            <ComponentLabel>
                                Estados de técnicos
                            </ComponentLabel>

                            <div className="flex flex-wrap gap-3">
                                <TechnicianStatusBadge status="En campo" />
                                <TechnicianStatusBadge status="Disponible" />
                                <TechnicianStatusBadge status="Ocupado" />
                            </div>
                        </div>
                    </div>
                </Section>

                {/* STAT CARDS */}
                <Section title="StatCard">
                    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
                        <StatCard
                            title="Pendientes"
                            value={3}
                            icon={Clock3}
                            valueColor="text-yellow-400"
                            iconColor="text-yellow-400"
                            borderColor="border-yellow-500/30"
                        />

                        <StatCard
                            title="Asignadas"
                            value={1}
                            icon={UserRound}
                            valueColor="text-blue-400"
                            borderColor="border-blue-500/30"
                        />

                        <StatCard
                            title="En ejecución"
                            value={1}
                            icon={Settings}
                            valueColor="text-pink-400"
                            borderColor="border-purple-500/30"
                        />

                        <StatCard
                            title="Finalizadas hoy"
                            value={2}
                            icon={Check}
                            valueColor="text-emerald-400"
                            borderColor="border-emerald-500/30"
                        />
                    </div>
                </Section>

                {/* TABLE */}
                <Section title="Table">
                    <Table
                        columns={technicianColumns}
                        data={technicians}
                    />
                </Section>

                {/* LOADING */}
                <Section title="Loading">
                    <div className="rounded-lg border border-slate-800 bg-slate-900">
                        <Loading text="Cargando técnicos..." />
                    </div>
                </Section>

                {/* ERROR */}
                <Section title="ErrorMessage">
                    <ErrorMessage
                        message="No fue posible cargar las órdenes de trabajo."
                        onRetry={() => console.log("Reintentar")}
                    />
                </Section>

                {/* EMPTY STATE */}
                <Section title="EmptyState">
                    <EmptyState
                        title="No hay órdenes de trabajo"
                        description="No existen órdenes que coincidan con los filtros seleccionados."
                        action={
                            <Button>
                                Crear OT
                            </Button>
                        }
                    />
                </Section>

                {/* MODAL */}
                <Section title="Modal">
                    <Button onClick={() => setModalOpen(true)}>
                        Abrir modal
                    </Button>

                    <Modal
                        isOpen={modalOpen}
                        onClose={() => setModalOpen(false)}
                        title="Crear orden de trabajo"
                    >
                        <div className="space-y-4">
                            <Input
                                label="Título"
                                placeholder="Mantenimiento preventivo"
                            />

                            <Select
                                label="Prioridad"
                                options={[
                                    {
                                        label: "Crítica",
                                        value: "CRITICA",
                                    },
                                    {
                                        label: "Alta",
                                        value: "ALTA",
                                    },
                                    {
                                        label: "Media",
                                        value: "MEDIA",
                                    },
                                ]}
                            />

                            <div className="flex justify-end gap-2 pt-2">
                                <Button
                                    variant="secondary"
                                    onClick={() => setModalOpen(false)}
                                >
                                    Cancelar
                                </Button>

                                <Button
                                    onClick={() => setModalOpen(false)}
                                >
                                    Crear OT
                                </Button>
                            </div>
                        </div>
                    </Modal>
                </Section>

            </div>
        </main>
    );
}

/* -------------------------------- */
/* Componentes auxiliares del demo */
/* -------------------------------- */

interface SectionProps {
    title: string;
    children: React.ReactNode;
}

function Section({
    title,
    children,
}: SectionProps) {
    return (
        <section className="mb-8">
            <h2 className="mb-4 border-b border-slate-800 pb-2 text-lg font-semibold text-white">
                {title}
            </h2>

            {children}
        </section>
    );
}

function ComponentLabel({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <p className="mb-3 text-sm text-slate-500">
            {children}
        </p>
    );
}