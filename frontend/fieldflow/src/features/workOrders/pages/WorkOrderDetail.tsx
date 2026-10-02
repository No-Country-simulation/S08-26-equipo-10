import { ArrowLeft, Pencil, Users } from "lucide-react";
import { useNavigate, useParams } from "react-router";
import { useState } from "react";

import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { StatusBadge } from "@/components/ui/StatusBadge";
import { PriorityBadge } from "@/components/ui/PriorityBadge";
import { DetailCard } from "@/components/ui/DetailCard";
import { DetailRow } from "@/components/ui/DetailRow";
import { useWorkOrderById } from "@/features/workOrders/hook/useWorkOrderById";
import { Modal } from "@/components/common/modal";
import { FormAsignacion } from "../components/TechniquesForm";

export default function OrdenTrabajoDetailPage() {
    const navigate = useNavigate();
    const { id } = useParams();

    const [activeTab, setActiveTab] = useState("information");
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedInterventionId, setSelectedInterventionId] = useState<
        string | null
    >(null);

    const {
        data: workOrder,
        isLoading,
        isError,
    } = useWorkOrderById(id ?? "");

    const tabs = [
        { id: "information", label: "Información" },
        { id: "checklist", label: "Checklist" },
        { id: "interventions", label: "Intervenciones" },
        { id: "components", label: "Componentes" },
        { id: "notes", label: "Notas técnicas" },
        { id: "evidence", label: "Evidencias" },
        { id: "conformity", label: "Conformidad" },
    ];

    if (isLoading) {
        return (
            <div className="flex min-h-100 items-center justify-center">
                <p className="text-slate-400">
                    Cargando orden de trabajo...
                </p>
            </div>
        );
    }

    if (isError || !workOrder) {
        return (
            <div className="flex min-h-100 flex-col items-center justify-center gap-4">
                <p className="text-slate-400">
                    No se pudo cargar la orden de trabajo.
                </p>

                <Button
                    onClick={() =>
                        navigate("/fieldflow/ordenesDeTrabajo")
                    }
                >
                    Volver a órdenes
                </Button>
            </div>
        );
    }

    /*
     * Una OT puede tener cero, una o múltiples intervenciones.
     */
    const interventions = workOrder.interventions ?? [];

    /*
     * Si todavía no hay una intervención seleccionada,
     * seleccionamos automáticamente la primera.
     */
    const selectedIntervention =
        interventions.find(
            (intervention) =>
                intervention.id === selectedInterventionId
        ) ?? interventions[0];

    /*
     * Cuando cambia la intervención seleccionada,
     * usamos esta como referencia para las pestañas
     * relacionadas con la intervención.
     */
    const handleSelectIntervention = (interventionId: string) => {
        setSelectedInterventionId(interventionId);
    };

    return (
        <div className="space-y-6 p-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                    <Button
                        variant="ghost"
                        onClick={() =>
                            navigate("/fieldflow/ordenesDeTrabajo")
                        }
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

                <div className="flex items-center gap-2">
                    {!workOrder.assignment?.technician && (
                        <Button onClick={() => setModalOpen(true)}>
                            <Users size={16} />
                            Asignar técnico
                        </Button>
                    )}
                    <Button>
                        <Pencil size={16} />
                        Editar
                    </Button>

                </div>
            </div>

            {/* Summary */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
                <DetailCard title="Tipo de servicio">
                    <p className="text-white">
                        {workOrder.serviceType?.name ??
                            "Sin tipo de servicio"}
                    </p>
                </DetailCard>

                <DetailCard title="Equipo">
                    <p className="text-white">
                        {workOrder.equipment?.name ?? "Sin equipo"}
                    </p>

                    <p className="text-sm text-slate-400">
                        {workOrder.equipment?.identifier ??
                            "Sin identificador"}
                    </p>
                </DetailCard>

                <DetailCard title="Técnico asignado">
                    <p className="text-white">
                        {workOrder.assignment?.technician?.name ??
                            "Sin asignar"}
                    </p>
                </DetailCard>

                <DetailCard title="Duración estimada">
                    <p className="text-white">
                        {workOrder.estimatedDurationMinutes ?? 0} minutos
                    </p>
                </DetailCard>
            </div>

            {/* Tabs */}
            <div className="border-b border-slate-700">
                <div className="flex gap-6 overflow-x-auto">
                    {tabs.map((tab) => (
                        <button
                            key={tab.id}
                            onClick={() => setActiveTab(tab.id)}
                            className={`cursor-pointer border-b-2 px-1 py-3 text-sm font-medium whitespace-nowrap ${activeTab === tab.id
                                ? "border-blue-500 text-blue-400"
                                : "border-transparent text-slate-400 hover:text-white"
                                }`}
                        >
                            {tab.label}
                        </button>
                    ))}
                </div>
            </div>

            {/* ========================================================= */}
            {/* INFORMACIÓN                                              */}
            {/* ========================================================= */}

            {activeTab === "information" && (
                <div className="space-y-6">
                    <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
                        {/* Cliente */}
                        <DetailCard title="Cliente">
                            <div className="space-y-3">
                                <DetailRow
                                    label="Nombre"
                                    value={
                                        workOrder.equipment?.client?.name ??
                                        "Sin cliente"
                                    }
                                />

                                <DetailRow
                                    label="ID"
                                    value={
                                        workOrder.equipment?.client?.id ??
                                        "Sin ID"
                                    }
                                />
                            </div>
                        </DetailCard>

                        {/* Sede */}
                        <DetailCard title="Sede">
                            <div className="space-y-3">
                                <DetailRow
                                    label="Nombre"
                                    value={
                                        workOrder.equipment?.site?.name ??
                                        "Sin sede"
                                    }
                                />

                                <DetailRow
                                    label="Dirección"
                                    value={
                                        workOrder.equipment?.site?.address ??
                                        "Sin dirección"
                                    }
                                />

                                <DetailRow
                                    label="ID"
                                    value={
                                        workOrder.equipment?.site?.id ??
                                        "Sin ID"
                                    }
                                />
                            </div>
                        </DetailCard>

                        {/* Instalación */}
                        {workOrder.equipment?.installation ? (
                            <DetailCard title="Instalación">
                                <div className="space-y-3">
                                    <DetailRow
                                        label="Nombre"
                                        value={
                                            workOrder.equipment.installation
                                                .name
                                        }
                                    />

                                    <DetailRow
                                        label="ID"
                                        value={
                                            workOrder.equipment.installation
                                                .id
                                        }
                                    />
                                </div>
                            </DetailCard>
                        ) : (
                            <EmptyState
                                title="Instalación"
                                description="No hay instalación asociada a este equipo."
                            />
                        )}

                        {/* Equipo */}
                        <DetailCard title="Equipo">
                            <div className="space-y-3">
                                <DetailRow
                                    label="Nombre"
                                    value={
                                        workOrder.equipment?.name ??
                                        "Sin nombre"
                                    }
                                />

                                <DetailRow
                                    label="Identificador"
                                    value={
                                        workOrder.equipment?.identifier ??
                                        "Sin identificador"
                                    }
                                />

                                <DetailRow
                                    label="Estado"
                                    value={
                                        workOrder.equipment?.currentStatus ??
                                        "Sin estado"
                                    }
                                />

                                <DetailRow
                                    label="ID"
                                    value={
                                        workOrder.equipment?.id ?? "Sin ID"
                                    }
                                />
                            </div>
                        </DetailCard>

                        {/* Servicio */}
                        <DetailCard title="Servicio">
                            <div className="space-y-3">
                                <DetailRow
                                    label="Tipo"
                                    value={
                                        workOrder.serviceType?.name ??
                                        "Sin tipo"
                                    }
                                />

                                <DetailRow
                                    label="ID"
                                    value={
                                        workOrder.serviceType?.id ??
                                        "Sin ID"
                                    }
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
                        {workOrder.assignment ? (
                            <DetailCard title="Asignación">
                                <div className="space-y-3">
                                    <DetailRow
                                        label="Técnico"
                                        value={
                                            workOrder.assignment
                                                ?.technician?.name ??
                                            "Sin asignar"
                                        }
                                    />

                                    <DetailRow
                                        label="Inicio"
                                        value={
                                            workOrder.assignment
                                                ?.plannedStartAt
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
                                                ?.plannedEndAt
                                                ? new Date(
                                                    workOrder.assignment.plannedEndAt
                                                ).toLocaleString()
                                                : "Sin programar"
                                        }
                                    />
                                </div>
                            </DetailCard>
                        ) : (
                            <EmptyState
                                title="Asignación"
                                description="No hay asignación para esta orden de trabajo."
                            />
                        )}
                    </div>

                    {/* Instrucciones */}
                    {workOrder.instructions && (
                        <DetailCard title="Instrucciones">
                            <p className="text-sm leading-6 text-slate-300">
                                {workOrder.instructions}
                            </p>
                        </DetailCard>
                    )}
                </div>
            )}

            {/* ========================================================= */}
            {/* CHECKLIST                                                */}
            {/* ========================================================= */}

            {activeTab === "checklist" && (
                <div className="space-y-6">
                    {workOrder.checklist ? (
                        <DetailCard title="Checklist">
                            <div className="space-y-3">
                                <p className="font-medium text-white">
                                    {workOrder.checklist.name}
                                </p>

                                {workOrder.checklist.items?.length ? (
                                    workOrder.checklist.items.map((item) => (
                                        <div
                                            key={item.id}
                                            className="flex items-center gap-3 rounded-lg border border-slate-700 bg-slate-800/50 p-3"
                                        >
                                            <div className="h-2 w-2 rounded-full bg-slate-400" />

                                            <span className="text-sm text-slate-300">
                                                {item.label}
                                            </span>
                                        </div>
                                    ))
                                ) : (
                                    <p className="py-4 text-sm text-slate-500">
                                        No hay elementos en este checklist.
                                    </p>
                                )}
                            </div>
                        </DetailCard>
                    ) : (
                        <EmptyState
                            title="Checklist"
                            description="Esta orden no tiene un checklist asociado."
                        />
                    )}

                    {/* Resultados del checklist */}
                    {selectedIntervention?.checklistAnswers?.length ? (
                        <DetailCard title="Resultados del checklist">
                            <div className="space-y-3">
                                {selectedIntervention.checklistAnswers.map(
                                    (answer) => (
                                        <div
                                            key={answer.id}
                                            className="rounded-lg border border-slate-700 p-4"
                                        >
                                            <div className="flex items-center justify-between gap-4">
                                                <p className="text-sm font-medium text-white">
                                                    {answer.item?.label ??
                                                        "Ítem del checklist"}
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
                                    )
                                )}
                            </div>
                        </DetailCard>
                    ) : (
                        <EmptyState
                            title="Resultados del checklist"
                            description={
                                selectedIntervention
                                    ? "La intervención seleccionada todavía no tiene respuestas registradas."
                                    : "No hay intervenciones registradas."
                            }
                        />
                    )}
                </div>
            )}

            {/* ========================================================= */}
            {/* INTERVENCIONES                                           */}
            {/* ========================================================= */}

            {activeTab === "interventions" && (
                <div className="space-y-6">
                    {interventions.length === 0 ? (
                        <EmptyState
                            title="Intervenciones"
                            description="Esta orden de trabajo todavía no tiene intervenciones registradas."
                        />
                    ) : (
                        <>
                            {/* Selector de intervenciones */}
                            <DetailCard title="Intervenciones registradas">
                                <div className="space-y-3">
                                    {interventions.map(
                                        (intervention, index) => {
                                            const isSelected =
                                                intervention.id ===
                                                selectedIntervention?.id;

                                            return (
                                                <button
                                                    key={intervention.id}
                                                    type="button"
                                                    onClick={() =>
                                                        handleSelectIntervention(
                                                            intervention.id
                                                        )
                                                    }
                                                    className={`w-full rounded-lg border p-4 text-left transition ${isSelected
                                                        ? "border-blue-500 bg-blue-500/10"
                                                        : "border-slate-700 bg-slate-800/30 hover:border-slate-600"
                                                        }`}
                                                >
                                                    <div className="flex items-center justify-between gap-4">
                                                        <div>
                                                            <p className="font-medium text-white">
                                                                Intervención{" "}
                                                                {index + 1}
                                                            </p>

                                                            <p className="mt-1 text-sm text-slate-400">
                                                                Técnico:{" "}
                                                                {intervention
                                                                    .technician
                                                                    ?.name ??
                                                                    "Sin técnico"}
                                                            </p>
                                                        </div>

                                                        <span className="rounded-full bg-slate-700 px-3 py-1 text-xs text-slate-300">
                                                            {
                                                                intervention.status
                                                            }
                                                        </span>
                                                    </div>
                                                </button>
                                            );
                                        }
                                    )}
                                </div>
                            </DetailCard>

                            {/* Detalle de la intervención seleccionada */}
                            {selectedIntervention && (
                                <>
                                    <DetailCard title="Detalle de la intervención">
                                        <div className="space-y-3">
                                            <DetailRow
                                                label="Técnico"
                                                value={
                                                    selectedIntervention
                                                        .technician?.name ??
                                                    "Sin técnico"
                                                }
                                            />

                                            <DetailRow
                                                label="Estado"
                                                value={
                                                    selectedIntervention.status
                                                }
                                            />

                                            <DetailRow
                                                label="Inicio"
                                                value={
                                                    selectedIntervention.startedAt
                                                        ? new Date(
                                                            selectedIntervention.startedAt
                                                        ).toLocaleString()
                                                        : "Sin fecha"
                                                }
                                            />

                                            <DetailRow
                                                label="Finalización"
                                                value={
                                                    selectedIntervention.endedAt
                                                        ? new Date(
                                                            selectedIntervention.endedAt
                                                        ).toLocaleString()
                                                        : "Sin fecha"
                                                }
                                            />

                                            <DetailRow
                                                label="Resultado"
                                                value={
                                                    selectedIntervention.result ??
                                                    "Sin resultado"
                                                }
                                            />

                                            <DetailRow
                                                label="Observaciones"
                                                value={
                                                    selectedIntervention.observations ??
                                                    "Sin observaciones"
                                                }
                                            />
                                        </div>
                                    </DetailCard>

                                    {/* Fallas y reparaciones */}
                                    <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
                                        <DetailCard title="Fallas detectadas">
                                            <div className="space-y-3">
                                                {selectedIntervention.failures
                                                    ?.length ? (
                                                    selectedIntervention.failures.map(
                                                        (failure) => (
                                                            <div
                                                                key={failure.id}
                                                                className="rounded-lg border border-slate-700 p-3"
                                                            >
                                                                <p className="text-sm text-slate-300">
                                                                    {
                                                                        failure.description
                                                                    }
                                                                </p>
                                                            </div>
                                                        )
                                                    )
                                                ) : (
                                                    <p className="text-sm text-slate-500">
                                                        No se registraron
                                                        fallas.
                                                    </p>
                                                )}
                                            </div>
                                        </DetailCard>

                                        <DetailCard title="Reparaciones">
                                            <div className="space-y-3">
                                                {selectedIntervention.repairs
                                                    ?.length ? (
                                                    selectedIntervention.repairs.map(
                                                        (repair) => (
                                                            <div
                                                                key={repair.id}
                                                                className="rounded-lg border border-slate-700 p-3"
                                                            >
                                                                <p className="text-sm text-slate-300">
                                                                    {
                                                                        repair.description
                                                                    }
                                                                </p>
                                                            </div>
                                                        )
                                                    )
                                                ) : (
                                                    <p className="text-sm text-slate-500">
                                                        No se registraron
                                                        reparaciones.
                                                    </p>
                                                )}
                                            </div>
                                        </DetailCard>
                                    </div>
                                </>
                            )}
                        </>
                    )}
                </div>
            )}

            {/* ========================================================= */}
            {/* COMPONENTES                                              */}
            {/* ========================================================= */}

            {activeTab === "components" && (
                <DetailCard title="Componentes intervenidos">
                    {selectedIntervention?.components?.length ? (
                        <div className="overflow-x-auto">
                            <table className="w-full text-left text-sm">
                                <thead>
                                    <tr className="border-b border-slate-700 text-slate-400">
                                        <th className="px-4 py-3">
                                            Componente
                                        </th>
                                        <th className="px-4 py-3">Acción</th>
                                        <th className="px-4 py-3">
                                            Descripción
                                        </th>
                                    </tr>
                                </thead>

                                <tbody>
                                    {selectedIntervention.components.map(
                                        (component) => (
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
                                        )
                                    )}
                                </tbody>
                            </table>
                        </div>
                    ) : (
                        <p className="py-6 text-center text-sm text-slate-500">
                            {selectedIntervention
                                ? "No hay componentes intervenidos en esta intervención."
                                : "No hay intervenciones registradas."}
                        </p>
                    )}
                </DetailCard>
            )}

            {/* ========================================================= */}
            {/* NOTAS TÉCNICAS                                           */}
            {/* ========================================================= */}

            {activeTab === "notes" && (
                <DetailCard title="Notas técnicas">
                    {selectedIntervention?.technicalNotes?.length ? (
                        <div className="space-y-3">
                            {selectedIntervention.technicalNotes.map(
                                (note) => (
                                    <div
                                        key={note.id}
                                        className="rounded-lg border border-slate-700 p-3"
                                    >
                                        <p className="text-sm text-slate-300">
                                            {note.content}
                                        </p>
                                    </div>
                                )
                            )}
                        </div>
                    ) : (
                        <p className="py-6 text-center text-sm text-slate-500">
                            {selectedIntervention
                                ? "No hay notas técnicas."
                                : "No hay intervenciones registradas."}
                        </p>
                    )}
                </DetailCard>
            )}

            {/* ========================================================= */}
            {/* EVIDENCIAS                                               */}
            {/* ========================================================= */}

            {activeTab === "evidence" && (
                <DetailCard title="Evidencias">
                    {selectedIntervention?.evidence?.length ? (
                        <div className="space-y-3">
                            {selectedIntervention.evidence.map((evidence) => (
                                <div
                                    key={evidence.id}
                                    className="rounded-lg border border-slate-700 p-4"
                                >
                                    <div className="flex items-center justify-between gap-4">
                                        <span className="text-sm font-medium text-white">
                                            {evidence.type}
                                        </span>

                                        <img className="h-32 w-32 object-cover" src={evidence.url} alt={evidence.description} />

                                    </div>

                                    <p className="mt-2 text-sm text-slate-400">
                                        {evidence.description}
                                    </p>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="py-6 text-center text-sm text-slate-500">
                            {selectedIntervention
                                ? "No hay evidencias registradas."
                                : "No hay intervenciones registradas."}
                        </p>
                    )}
                </DetailCard>
            )}

            {/* ========================================================= */}
            {/* CONFORMIDAD                                               */}
            {/* ========================================================= */}

            {activeTab === "conformity" && (
                <DetailCard title="Conformidad">
                    {selectedIntervention?.conformity ? (
                        <div className="space-y-3">
                            <DetailRow
                                label="Firma"
                                value={
                                    selectedIntervention.conformity
                                        .signature
                                }
                            />
                        </div>
                    ) : (
                        <p className="py-6 text-center text-sm text-slate-500">
                            {selectedIntervention
                                ? "No hay conformidad registrada."
                                : "No hay intervenciones registradas."}
                        </p>
                    )}
                </DetailCard>
            )}

            <Modal
                isOpen={modalOpen}
                onClose={() => setModalOpen(false)}
                title="Asignar técnico"
            >
                <FormAsignacion estimateDuration={workOrder.estimatedDurationMinutes} idWorkOrder={workOrder.id} onCancel={() => setModalOpen(false)} />
            </Modal>
        </div>
    );
}