import { ArrowLeft, Pencil } from "lucide-react";
import { useNavigate, useParams } from "react-router";

import { Button } from "@/components/common/Button";
import { StatusBadge } from "@/components/ui/StatusBadge";
import { PriorityBadge } from "@/components/ui/PriorityBadge";
import { DetailCard } from "@/components/ui/DetailCard";
import { DetailRow } from "@/components/ui/DetailRow";
import { useWorkOrderById } from "@/hooks/workorder/useWorkOrderById";
import { EmptyState } from "@/components/common/EmptyState";

export default function OrdenTrabajoDetailPage() {
    const navigate = useNavigate();
    const { id } = useParams();

    const {
        data: workOrder,
        isLoading,
        isError,
    } = useWorkOrderById(id ?? "");

    if (isLoading) {
        return (
            <div className="flex min-h-100 items-center justify-center">
                <p className="text-slate-400">Cargando orden de trabajo...</p>
            </div>
        );
    }

    if (isError || !workOrder) {
        return (
            <div className="flex min-h-100 flex-col items-center justify-center gap-4">
                <p className="text-slate-400">
                    No se pudo cargar la orden de trabajo.
                </p>

                <Button onClick={() => navigate("/fieldflow/ordenesDeTrabajo")}>
                    Volver a órdenes
                </Button>
            </div>
        );
    }

    const intervention = workOrder.interventions?.[0];

    return (
        <div className="space-y-6 p-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                    <Button
                        variant="ghost"

                        onClick={() => navigate("/fieldflow/ordenesDeTrabajo")}
                    >
                        <ArrowLeft size={20} />
                    </Button>

                    <div>
                        <div className="flex items-center gap-3">
                            <h1 className="text-2xl font-semibold text-white">
                                Orden de Trabajo
                            </h1>

                            <StatusBadge status={workOrder.status} />
                            <PriorityBadge priority={workOrder.priority} />
                        </div>

                        <p className="mt-1 text-sm text-slate-400">
                            OT #{workOrder.id}
                        </p>
                    </div>
                </div>

                <Button>
                    <Pencil size={16} />
                    Editar
                </Button>
            </div>

            {/* Summary */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
                <DetailCard title="Tipo de servicio">
                    <p className="text-white">
                        {workOrder.serviceType.name}
                    </p>
                </DetailCard>

                <DetailCard title="Equipo">
                    <p className="text-white">
                        {workOrder.equipment.name}
                    </p>

                    <p className="text-sm text-slate-400">
                        {workOrder.equipment.identifier}
                    </p>
                </DetailCard>

                <DetailCard title="Técnico asignado">
                    <p className="text-white">
                        {workOrder.assignment?.technician.name ?? "Sin asignar"}
                    </p>
                </DetailCard>

                <DetailCard title="Duración estimada">
                    <p className="text-white">
                        {workOrder.estimatedDurationMinutes} minutos
                    </p>
                </DetailCard>
            </div>

            {/* Tabs */}
            <div className="border-b border-slate-700">
                <div className="flex gap-6 overflow-x-auto">
                    <button className="border-b-2 border-blue-500 px-1 py-3 text-sm font-medium text-blue-400">
                        Información
                    </button>

                    <button className="px-1 py-3 text-sm text-slate-400 hover:text-white">
                        Checklist
                    </button>

                    <button className="px-1 py-3 text-sm text-slate-400 hover:text-white">
                        Intervenciones
                    </button>

                    <button className="px-1 py-3 text-sm text-slate-400 hover:text-white">
                        Notas técnicas
                    </button>

                    <button className="px-1 py-3 text-sm text-slate-400 hover:text-white">
                        Evidencias
                    </button>

                    <button className="px-1 py-3 text-sm text-slate-400 hover:text-white">
                        Conformidad
                    </button>
                </div>
            </div>

            {/* Información */}
            <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
                {/* Cliente */}
                <DetailCard title="Cliente">
                    <div className="space-y-3">
                        <DetailRow
                            label="Nombre"
                            value={workOrder.equipment.client.name}
                        />

                        <DetailRow
                            label="ID"
                            value={workOrder.equipment.client.id}
                        />
                    </div>
                </DetailCard>

                {/* Sede */}
                <DetailCard title="Sede">
                    <div className="space-y-3">
                        <DetailRow
                            label="Nombre"
                            value={workOrder.equipment.site.name}
                        />

                        <DetailRow
                            label="Dirección"
                            value={workOrder.equipment.site.address}
                        />

                        <DetailRow
                            label="ID"
                            value={workOrder.equipment.site.id}
                        />
                    </div>
                </DetailCard>

                {/* Instalación */}
                {workOrder.equipment.installation ? <DetailCard title="Instalación">
                    <div className="space-y-3">
                        <DetailRow
                            label="Nombre"
                            value={workOrder.equipment.installation.name}
                        />

                        <DetailRow
                            label="ID"
                            value={workOrder.equipment.installation.id}
                        />
                    </div>
                </DetailCard> : <EmptyState title="Instalación" description="No hay instalación asociada a este equipo." />}

                {/* Equipo */}
                <DetailCard title="Equipo">
                    <div className="space-y-3">
                        <DetailRow
                            label="Nombre"
                            value={workOrder.equipment.name}
                        />

                        <DetailRow
                            label="Identificador"
                            value={workOrder.equipment.identifier}
                        />

                        <DetailRow
                            label="Estado"
                            value={workOrder.equipment.currentStatus}
                        />

                        <DetailRow
                            label="ID"
                            value={workOrder.equipment.id}
                        />
                    </div>
                </DetailCard>

                {/* Servicio */}
                <DetailCard title="Servicio">
                    <div className="space-y-3">
                        <DetailRow
                            label="Tipo"
                            value={workOrder.serviceType.name}
                        />

                        <DetailRow
                            label="ID"
                            value={workOrder.serviceType.id}
                        />

                        <DetailRow
                            label="Prioridad"
                            value={workOrder.priority}
                        />

                        <DetailRow
                            label="Estado"
                            value={workOrder.status}
                        />
                    </div>
                </DetailCard>

                {/* Asignación */}
                {workOrder.assignment ? <DetailCard title="Asignación">
                    <div className="space-y-3">
                        <DetailRow
                            label="Técnico"
                            value={
                                workOrder.assignment?.technician.name ?? "Sin asignar"
                            }
                        />

                        <DetailRow
                            label="Inicio"
                            value={
                                workOrder.assignment
                                    ? new Date(
                                        workOrder.assignment.plannedStartAt
                                    ).toLocaleString()
                                    : "Sin programar"
                            }
                        />

                        <DetailRow
                            label="Fin"
                            value={
                                workOrder.assignment
                                    ? new Date(
                                        workOrder.assignment.plannedEndAt
                                    ).toLocaleString()
                                    : "Sin programar"
                            }
                        />
                    </div>
                </DetailCard> : <EmptyState title="Asignaciones" description="No hay asignación para esta orden de trabajo." />}
            </div>

            {/* Instrucciones */}
            <DetailCard title="Instrucciones">
                <p className="text-sm leading-6 text-slate-300">
                    {workOrder.instructions}
                </p>
            </DetailCard>

            {/* Checklist */}
            {workOrder.checklist && (<DetailCard title="Checklist">
                <div className="space-y-3">
                    <p className="font-medium text-white">
                        {workOrder.checklist.name}
                    </p>

                    {workOrder.checklist.items.map((item) => (
                        <div
                            key={item.id}
                            className="flex items-center gap-3 rounded-lg border border-slate-700 bg-slate-800/50 p-3"
                        >
                            <div className="h-2 w-2 rounded-full bg-slate-400" />

                            <span className="text-sm text-slate-300">
                                {item.label}
                            </span>
                        </div>
                    ))}
                </div>
            </DetailCard>)}

            {/* Intervención */}
            {intervention && (
                <DetailCard title="Intervención">
                    <div className="space-y-4">
                        <DetailRow
                            label="Técnico"
                            value={intervention.technician.name}
                        />

                        <DetailRow
                            label="Estado"
                            value={intervention.status}
                        />

                        <DetailRow
                            label="Inicio"
                            value={new Date(
                                intervention.startedAt
                            ).toLocaleString()}
                        />

                        <DetailRow
                            label="Finalización"
                            value={new Date(
                                intervention.endedAt
                            ).toLocaleString()}
                        />

                        <DetailRow
                            label="Resultado"
                            value={intervention.result}
                        />

                        <DetailRow
                            label="Observaciones"
                            value={intervention.observations}
                        />
                    </div>
                </DetailCard>
            )}

            {/* Fallas y reparaciones */}
            {intervention && (
                <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
                    <DetailCard title="Fallas detectadas">
                        <div className="space-y-3">
                            {intervention.failures.length > 0 ? (
                                intervention.failures.map((failure) => (
                                    <div
                                        key={failure.id}
                                        className="rounded-lg border border-slate-700 p-3"
                                    >
                                        <p className="text-sm text-slate-300">
                                            {failure.description}
                                        </p>
                                    </div>
                                ))
                            ) : (
                                <p className="text-sm text-slate-500">
                                    No se registraron fallas.
                                </p>
                            )}
                        </div>
                    </DetailCard>

                    <DetailCard title="Reparaciones">
                        <div className="space-y-3">
                            {intervention.repairs.length > 0 ? (
                                intervention.repairs.map((repair) => (
                                    <div
                                        key={repair.id}
                                        className="rounded-lg border border-slate-700 p-3"
                                    >
                                        <p className="text-sm text-slate-300">
                                            {repair.description}
                                        </p>
                                    </div>
                                ))
                            ) : (
                                <p className="text-sm text-slate-500">
                                    No se registraron reparaciones.
                                </p>
                            )}
                        </div>
                    </DetailCard>
                </div>
            )}

            {/* Componentes */}
            {intervention ? (
                <DetailCard title="Componentes intervenidos">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left text-sm">
                            <thead>
                                <tr className="border-b border-slate-700 text-slate-400">
                                    <th className="px-4 py-3">Componente</th>
                                    <th className="px-4 py-3">Acción</th>
                                    <th className="px-4 py-3">Descripción</th>
                                </tr>
                            </thead>

                            <tbody>
                                {intervention.components.map((component) => (
                                    <tr
                                        key={component.id}
                                        className="border-b border-slate-800"
                                    >
                                        <td className="px-4 py-3 text-white">
                                            {component.componentName}
                                        </td>

                                        <td className="px-4 py-3 text-slate-300">
                                            {component.action}
                                        </td>

                                        <td className="px-4 py-3 text-slate-400">
                                            {component.description}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </DetailCard>
            ) : (
                <EmptyState title="Intervenciónes" description="No se ha registrado ninguna intervención para este orden de trabajo." />
            )}

            {/* Respuestas del checklist */}
            {intervention && (
                <DetailCard title="Resultados del checklist">
                    <div className="space-y-3">
                        {intervention.checklistAnswers.map((answer) => (
                            <div
                                key={answer.id}
                                className="rounded-lg border border-slate-700 p-4"
                            >
                                <div className="flex items-center justify-between gap-4">
                                    <p className="text-sm font-medium text-white">
                                        {answer.item.label}
                                    </p>

                                    <span className="text-xs text-slate-400">
                                        {answer.value}
                                    </span>
                                </div>

                                {answer.observation && (
                                    <p className="mt-2 text-sm text-slate-400">
                                        {answer.observation}
                                    </p>
                                )}
                            </div>
                        ))}
                    </div>
                </DetailCard>
            )}

            {/* Notas técnicas */}
            {intervention && (
                <DetailCard title="Notas técnicas">
                    <div className="space-y-3">
                        {intervention.technicalNotes.length > 0 ? (
                            intervention.technicalNotes.map((note) => (
                                <div
                                    key={note.id}
                                    className="rounded-lg border border-slate-700 p-3"
                                >
                                    <p className="text-sm text-slate-300">
                                        {note.content}
                                    </p>
                                </div>
                            ))
                        ) : (
                            <p className="text-sm text-slate-500">
                                No hay notas técnicas.
                            </p>
                        )}
                    </div>
                </DetailCard>
            )}

            {/* Evidencias */}
            {intervention && (
                <DetailCard title="Evidencias">
                    <div className="space-y-3">
                        {intervention.evidence.length > 0 ? (
                            intervention.evidence.map((evidence) => (
                                <div
                                    key={evidence.id}
                                    className="rounded-lg border border-slate-700 p-4"
                                >
                                    <div className="flex items-center justify-between">
                                        <span className="text-sm font-medium text-white">
                                            {evidence.type}
                                        </span>

                                        <span className="text-xs text-slate-500">
                                            {evidence.reference}
                                        </span>
                                    </div>

                                    <p className="mt-2 text-sm text-slate-400">
                                        {evidence.description}
                                    </p>
                                </div>
                            ))
                        ) : (
                            <p className="text-sm text-slate-500">
                                No hay evidencias registradas.
                            </p>
                        )}
                    </div>
                </DetailCard>
            )}

            {/* Conformidad */}
            {intervention?.conformity && (
                <DetailCard title="Conformidad">
                    <DetailRow
                        label="Firma"
                        value={intervention.conformity.signature}
                    />
                </DetailCard>
            )}
        </div>
    );
}