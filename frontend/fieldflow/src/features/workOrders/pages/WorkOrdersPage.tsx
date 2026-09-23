import { useMemo, useState } from "react";
import { Plus, Search } from "lucide-react";

import { Button } from "@/components/common/Button";
import { Select } from "@/components/common/Select";
import { Table, createColumn } from "@/components/common/Table";
import { PriorityBadge } from "@/components/ui/PriorityBadge";
import { StatusBadge } from "@/components/ui/StatusBadge";
import { useWorkOrders } from "@/features/workOrders/hook/useWorkOrders";
import type { WorkOrder } from "@/features/workOrders/types/workOrder";




const column = createColumn<WorkOrder>();


function OrdenesTrabajoPage() {
    const [search, setSearch] = useState("");
    const [status, setStatus] = useState("");
    const [priority, setPriority] = useState("");
    const [type, setType] = useState("");
    const {
        data: workOrders = [],
        isLoading,
        isError,
    } = useWorkOrders();



    const filteredOrders = useMemo(() => {
        const normalizedSearch = search.toLowerCase().trim();

        return workOrders.filter((order) => {
            const matchesSearch =
                !normalizedSearch ||
                order.id.toLowerCase().includes(normalizedSearch) ||
                order.equipment.identifier.toLowerCase().includes(normalizedSearch) ||
                order.equipment.name.toLowerCase().includes(normalizedSearch) ||
                order.serviceType.name.toLowerCase().includes(normalizedSearch);

            const matchesStatus =
                !status || order.status === status;

            const matchesPriority =
                !priority || order.priority === priority;

            const matchesType =
                !type || order.serviceType.name === type;

            return (
                matchesSearch &&
                matchesStatus &&
                matchesPriority &&
                matchesType
            );
        });
    }, [workOrders, search, status, priority, type]);

    const columns = [
        column({
            header: "NÚMERO",
            accessor: "id",
            render: (value) => (
                <span className="font-mono text-xs  text-cyan-400">
                    {value}
                </span>
            ),
        }),

        column({
            header: "CLIENTE / SEDE",
            accessor: "equipment",
            render: (value, row) => (
                <div>
                    <p className="font-semibold text-white">
                        {value.name}
                    </p>

                    <p className="text-sm text-slate-400">
                        {row.equipment.identifier}
                    </p>
                </div>
            ),
        }),

        column({
            header: "EQUIPO",
            accessor: "equipment",
            render: (value, row) => (
                <div>
                    <p className="font-medium text-white">
                        {value.name}
                    </p>

                    <p className="text-sm text-slate-400">
                        {row.equipment.identifier}
                    </p>
                </div>
            ),
        }),

        column({
            header: "TIPO",
            accessor: "serviceType",
            render: (value) => (
                <span className="font-semibold text-blue-400">
                    {value.name}
                </span>
            ),
        }),

        column({
            header: "PRIORIDAD",
            accessor: "priority",
            render: (value) => (
                <div className="flex items-center justify-center gap-2">
                    <PriorityBadge priority={value} />
                </div>
            ),
        }),


        column({
            header: "TÉCNICO",
            accessor: "serviceType",
            render: (value) => (
                <span
                    className={
                        value
                            ? "text-white"
                            : "text-slate-500"
                    }
                >
                    {value.instructions ?? "Sin asignar"}
                </span>
            ),
        }),

        column({
            header: "FECHA",
            accessor: "estimatedDurationMinutes",
            render: (value, row) => (
                <div className="font-mono text-xs flex flex-col gap-1 items-center">
                    <p className="text-blue-300">
                        {value}
                    </p>

                    <p className="text-slate-500">
                        {row.estimatedDurationMinutes}
                    </p>
                </div>
            ),
        }),

        column({
            header: "ESTADO",
            accessor: "status",
            render: (value) => (
                <div className="flex items-center  gap-2">
                    <StatusBadge status={value} />
                </div>
            ),
        }),
    ];

    return (
        <section className="min-h-full bg-[#0b1015] p-6 text-white">
            {/* Header */}
            <div className="flex items-start justify-between gap-4 mb-4">
                <div>
                    <h1 className="text-2xl font-bold text-white">
                        Órdenes de Trabajo
                    </h1>

                    <p className="mt-1 text-sm text-slate-400">
                        {filteredOrders.length} de {workOrders.length} registros
                    </p>
                </div>

                <Button icon={Plus}>
                    Nueva OT
                </Button>
            </div>

            {/* Filters */}
            <div className="rounded-lg border border-slate-700 bg-slate-900/50 p-4 mt-1 mb-4 ">
                <div className="grid grid-cols-1 gap-3 lg:grid-cols-[minmax(280px,1fr)_250px_250px_200px] justify-center items-center">
                    <div className="relative">
                        <Search className="absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" />

                        <input
                            type="text"
                            value={search}
                            onChange={(event) =>
                                setSearch(event.target.value)
                            }
                            placeholder="Buscar por número, cliente, equipo..."
                            className="h-8 w-full rounded-md border border-slate-700 bg-slate-800 px-4 pl-11 text-sm text-white outline-none placeholder:text-slate-500 focus:border-cyan-500"
                        />
                    </div>
                    <Select
                        value={status}
                        onChange={(event) => setStatus(event.target.value)}
                        options={[
                            {
                                label: "Todos los estados",
                                value: "",
                            },
                            {
                                label: "Pendiente",
                                value: "PENDING",
                            },
                            {
                                label: "Asignada",
                                value: "ASSIGNED",
                            },
                            {
                                label: "En camino",
                                value: "EN_ROUTE",
                            },
                            {
                                label: "En ejecución",
                                value: "IN_PROGRESS",
                            },
                            {
                                label: "Pendiente conformidad",
                                value: "PENDING_CUSTOMER_CONFIRMATION",
                            },
                            {
                                label: "Finalizada",
                                value: "COMPLETED",
                            },
                            {
                                label: "Reprogramada",
                                value: "RESCHEDULED",
                            },
                        ]}
                    />

                    <Select
                        value={priority}
                        onChange={(event) =>
                            setPriority(event.target.value)
                        }
                        options={[
                            {
                                label: "Todos las prioridades",
                                value: "",
                            },
                            {
                                label: "Crítica",
                                value: "CRITICAL",
                            },
                            {
                                label: "Alta",
                                value: "HIGH",
                            },
                            {
                                label: "Media",
                                value: "MEDIUM",
                            },
                            {
                                label: "Baja",
                                value: "LOW",
                            }
                        ]}
                    />

                    <Select
                        value={type}
                        onChange={(event) =>
                            setType(event.target.value)
                        }
                        options={[
                            {
                                label: "Todos los tipos",
                                value: "",
                            },
                            {
                                label: "Correctivo",
                                value: "Correctivo",
                            },
                            {
                                label: "Preventivo",
                                value: "Preventivo",
                            },
                            {
                                label: "Inspección",
                                value: "Inspección",
                            },
                        ]}
                    />
                </div>
            </div>

            {/* Table */}
            <Table
                columns={columns}
                data={filteredOrders}
                isLoading={isLoading}
                isError={isError}
                errorMessage="Ocurrió un error al cargar las órdenes de trabajo"
                emptyMessage="No hay órdenes de trabajo para mostrar"
            />
        </section>
    );
}

export default OrdenesTrabajoPage;