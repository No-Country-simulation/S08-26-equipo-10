
import { useState } from "react";
import { ArrowLeft, Calendar, Clock, UserRound } from "lucide-react";
import { useNavigate } from "react-router";
import { Button } from "@/components/common/Button";
import { Select } from "@/components/common/Select";
import { PriorityBadge } from "@/components/ui/PriorityBadge";
import type { WorkOrderPriority } from "@/features/workOrders/types/workOrder";
import type { FormErrors } from "@/features/workOrders/types/workOrderFormError";
import FormField from "../components/FormField";
import InfoItem from "../components/InfoItem";
import { useWorkOrdersServiceType } from "../hook/userWorkOderServiceType";
import { useEquipment } from "@/features/equipment/hook/useEquiments";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Loading } from "@/components/common/loading";
import { useTechniques } from "@/features/technicians/hook/useTechniques";






function AddWorkOrderPage() {
    const navigate = useNavigate();

    const [errors, setErrors] = useState<FormErrors>({});
    const [apiError, setApiError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [equipmentId, setEquipmentId] = useState("");
    const [serviceTypeId, setServiceTypeId] = useState("");
    const [estimatedDuration, setEstimatedDuration] = useState("");
    const [priority, setPriority] = useState<WorkOrderPriority>("MEDIUM");

    const [schedule, setSchedule] = useState(false);
    const [date, setDate] = useState("");
    const [time, setTime] = useState("");
    const [technicianId, setTechnicianId] = useState("");

    const [instructions, setInstructions] = useState("");

    const {
        data: workOrderServicesType = [],
        isLoading: loadingWorkOrderService,
        isError: errorWorkOrderService,
    } = useWorkOrdersServiceType();

    const {
        data: equipments = [],
        isLoading: loadingEquipment,
        isError: errorEquipment,
    } = useEquipment();

    const {
        data: techniques = [],

    } = useTechniques();


    const selectedEquipment = equipments.find(
        (equipment) => equipment.id === equipmentId
    );

    const canCreate =
        equipmentId &&
        serviceTypeId &&
        estimatedDuration &&
        instructions.trim();

    const validateForm = (): FormErrors => {
        const newErrors: FormErrors = {};

        if (!equipmentId) {
            newErrors.equipmentId = "Selecciona un equipo.";
        }

        if (!serviceTypeId) {
            newErrors.serviceTypeId = "Selecciona un tipo de servicio.";
        }

        const duration = Number(estimatedDuration);

        if (!estimatedDuration) {
            newErrors.estimatedDuration =
                "La duración estimada es obligatoria.";
        } else if (!Number.isInteger(duration) || duration <= 0) {
            newErrors.estimatedDuration =
                "La duración debe ser un número entero mayor a 0.";
        }

        if (schedule) {
            if (!date) {
                newErrors.date = "Selecciona una fecha.";
            }

            if (!time) {
                newErrors.time = "Selecciona una hora.";
            }

            if (!technicianId) {
                newErrors.technicianId =
                    "Selecciona un técnico disponible.";
            }
        }

        if (!instructions.trim()) {
            newErrors.instructions =
                "Las instrucciones son obligatorias.";
        }

        return newErrors;
    };

    const handleSubmit = async (
        event: React.FormEvent<HTMLFormElement>
    ) => {
        event.preventDefault();

        setApiError("");

        const validationErrors = validateForm();

        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            return;
        }

        const payload = {
            equipmentId,
            serviceTypeId,
            estimatedDurationMinutes: Number(estimatedDuration),
            priority,
            instructions: instructions.trim(),
            ...(schedule && {
                plannedStart: `${date}T${time}`,
                technicianId,
            }),
        };

        try {
            setIsSubmitting(true);



            navigate("/fieldflow/ordenesDeTrabajo");
        } catch (error) {
            setApiError(
                "No fue posible crear la orden de trabajo. Inténtalo nuevamente."
            );
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <section className="min-h-full p-6 text-white">
            {/* Header */}
            <div className="mb-6 flex items-start justify-between gap-4">
                <div className="flex items-start gap-3">
                    <button
                        type="button"
                        onClick={() => navigate("/fieldflow/ordenesDeTrabajo")}
                        className="mt-1 flex h-8 w-8 items-center cursor-pointer justify-center rounded-md text-slate-400 transition hover:bg-slate-900 hover:text-white"
                        aria-label="Volver"
                    >
                        <ArrowLeft className="h-4 w-4" />
                    </button>

                    <div>
                        <h1 className="text-2xl font-bold text-white">
                            Nueva Orden de Trabajo
                        </h1>

                        <p className="mt-1 text-base text-[#78a0d2]">
                            Crea una nueva orden para realizar mantenimiento
                            a un equipo registrado.
                        </p>
                    </div>
                </div>
            </div>
            {apiError && (
                <div className="rounded-md border border-red-900/50 bg-red-950/30 px-4 py-3 text-sm text-red-400">
                    {apiError}
                </div>
            )}

            <form
                onSubmit={handleSubmit}
                className=" max-w-6xl space-y-4 "
            >
                {/* Equipo */}
                <section className="rounded-lg border border-slate-700 bg-slate-900/50 p-5">
                    <div className="mb-5">
                        <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-300">
                            Información del equipo
                        </h2>

                        <p className="mt-1 text-sm text-slate-500">
                            Selecciona el equipo registrado al que se le
                            realizará el mantenimiento.
                        </p>
                    </div>

                    <div>
                        <label className="mb-2 block text-sm font-medium text-slate-300">
                            Equipo *
                        </label>

                        {loadingEquipment ? (
                            <Loading size="sm" text="Cargando equipos" />
                        ) : errorEquipment ? (
                            <ErrorMessage message="Error al cargar equipos" />
                        ) : (
                            <Select
                                value={equipmentId}
                                onChange={(event) => setEquipmentId(event.target.value)}
                                options={[
                                    {
                                        label: "Seleccionar equipo",
                                        value: "",
                                    },
                                    ...equipments.map((equipment) => ({
                                        label: `${equipment.identifier} — ${equipment.name}`,
                                        value: equipment.id,
                                    })),
                                ]}
                            />
                        )}
                        {errors.equipmentId && (
                            <FieldError message={errors.equipmentId} />
                        )}
                    </div>

                    {selectedEquipment && (
                        <div className="mt-4 grid grid-cols-1 gap-3 md:grid-cols-3">
                            <InfoItem
                                label="Cliente"
                                value={selectedEquipment.client.name}
                            />

                            <InfoItem
                                label="Sede"
                                value={
                                    selectedEquipment.site?.name ?? "—"
                                }
                            />

                            <InfoItem
                                label="Instalación"
                                value={
                                    selectedEquipment.installation?.name ?? "—"
                                }
                            />
                        </div>
                    )}
                </section>

                {/* Servicio */}
                <section className="rounded-lg border border-slate-700 bg-slate-900/50 p-5">
                    <div className="mb-5">
                        <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-300">
                            Servicio
                        </h2>

                        <p className="mt-1 text-sm text-slate-500">
                            Define el tipo de mantenimiento y sus parámetros.
                        </p>
                    </div>

                    <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                        <div>
                            <label className="mb-2 block text-sm font-medium text-slate-300">
                                Tipo de servicio *
                            </label>

                            {loadingWorkOrderService ? (<Loading size="sm" text="Cargando servicios" />) :
                                errorWorkOrderService ? (<ErrorMessage message="Error al cargar tipos de servicios" />) :
                                    workOrderServicesType && <Select
                                        value={serviceTypeId}
                                        onChange={(event) =>
                                            setServiceTypeId(event.target.value)
                                        }
                                        options={[
                                            {
                                                label: "Seleccionar tipo de servicio",
                                                value: "",
                                            },
                                            ...workOrderServicesType.map((service) => ({
                                                label: service.name,
                                                value: service.id,
                                            })),
                                        ]}
                                    />}

                            {errors.serviceTypeId && (
                                <FieldError message={errors.serviceTypeId} />
                            )}
                        </div>

                        <div>
                            <label className="mb-2 block text-sm font-medium text-slate-300">
                                Duración estimada *
                            </label>

                            <div className="relative">
                                <input
                                    type="number"
                                    min="1"
                                    value={estimatedDuration}
                                    onChange={(event) =>
                                        setEstimatedDuration(
                                            event.target.value
                                        )
                                    }
                                    placeholder="Ej. 120"
                                    className="h-8 w-full rounded-md border border-slate-700 bg-slate-800 px-3 pr-20 text-sm text-white outline-none placeholder:text-slate-500 focus:border-cyan-500"
                                />

                                <span className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-xs text-slate-500">
                                    minutos
                                </span>
                            </div>

                            {errors.estimatedDuration && <FieldError message={errors.estimatedDuration} />}
                        </div>
                    </div>

                    <div className="mt-5">
                        <label className="mb-3 block text-sm font-medium text-slate-300">
                            Prioridad *
                        </label>

                        <div className="flex flex-wrap gap-2">
                            {(
                                [
                                    "LOW",
                                    "MEDIUM",
                                    "HIGH",
                                    "CRITICAL",
                                ] as WorkOrderPriority[]
                            ).map((value) => (
                                <button
                                    key={value}
                                    type="button"
                                    onClick={() => setPriority(value)}
                                    className={`rounded-md border px-3 py-2 cursor-pointer transition ${priority === value
                                        ? "border-cyan-500 bg-slate-800"
                                        : "border-slate-700 bg-slate-900 hover:border-slate-600"
                                        }`}
                                >
                                    <PriorityBadge priority={value} />
                                </button>
                            ))}
                        </div>
                    </div>
                </section>

                {/* Programación */}
                <section className="rounded-lg border border-slate-700 bg-slate-900/50 p-5">
                    <div className="mb-5">
                        <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-300">
                            Programación
                        </h2>

                        <p className="mt-1 text-sm text-slate-500">
                            La programación es opcional. Puedes asignarla
                            posteriormente.
                        </p>
                    </div>

                    <label className="flex cursor-pointer items-center gap-3">
                        <input
                            type="checkbox"
                            checked={schedule}
                            onChange={(event) =>
                                setSchedule(event.target.checked)
                            }
                            className="h-4 w-4 accent-cyan-500"
                        />

                        <span className="text-sm text-slate-300">
                            Programar fecha y hora
                        </span>
                    </label>

                    {schedule && (
                        <div className="mt-5 space-y-5">
                            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                                <FormField
                                    label="Fecha"
                                    icon={Calendar}
                                >
                                    <input
                                        type="date"
                                        value={date}
                                        onChange={(event) =>
                                            setDate(event.target.value)
                                        }
                                        className="h-9 w-full rounded-md border border-slate-700 bg-slate-800 px-3 text-sm text-white outline-none focus:border-cyan-500"
                                    />
                                </FormField>

                                <FormField
                                    label="Hora"
                                    icon={Clock}
                                >
                                    <input
                                        type="time"
                                        value={time}
                                        onChange={(event) =>
                                            setTime(event.target.value)
                                        }
                                        className="h-9 w-full rounded-md border border-slate-700 bg-slate-800 px-3 text-sm text-white outline-none focus:border-cyan-500"
                                    />
                                </FormField>
                            </div>

                            <div>
                                <div className="mb-3 flex items-center justify-between">
                                    <div>
                                        <label className="text-sm font-medium text-slate-300">
                                            Técnicos disponibles
                                        </label>

                                        <p className="mt-1 text-xs text-slate-500">
                                            Selecciona el técnico que realizará
                                            la orden.
                                        </p>
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    {techniques.map((technician) => (
                                        <button
                                            key={technician.id}
                                            type="button"
                                            onClick={() =>
                                                setTechnicianId(technician.id)
                                            }
                                            className={`flex w-full items-center justify-between rounded-md border p-3 text-left transition ${technicianId === technician.id
                                                ? "border-cyan-500 bg-slate-800"
                                                : "border-slate-700 bg-slate-900 hover:border-slate-600"
                                                }`}
                                        >
                                            <div className="flex items-center gap-3">
                                                <div className="flex h-8 w-8 items-center justify-center rounded-full bg-slate-800 text-slate-400">
                                                    <UserRound className="h-4 w-4" />
                                                </div>

                                                <div>
                                                    <p className="text-sm font-medium text-white">
                                                        {technician.name}
                                                    </p>

                                                    <p className="text-xs text-slate-500">
                                                        {technician.id}
                                                    </p>
                                                </div>
                                            </div>

                                            <span className="text-xs text-emerald-400">
                                                Disponible
                                            </span>
                                        </button>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}
                </section>

                {/* Instrucciones */}
                <section className="rounded-lg border border-slate-700 bg-slate-900/50 p-5">
                    <div className="mb-5">
                        <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-300">
                            Instrucciones
                        </h2>

                        <p className="mt-1 text-sm text-slate-500">
                            Agrega información específica que el técnico deba
                            conocer para realizar esta orden.
                        </p>
                    </div>

                    <textarea
                        value={instructions}
                        onChange={(event) =>
                            setInstructions(event.target.value)
                        }
                        rows={5}
                        placeholder="Escriba las instrucciones específicas para esta orden..."
                        className="w-full resize-none rounded-md border border-slate-700 bg-slate-800 p-3 text-sm text-white outline-none placeholder:text-slate-500 focus:border-cyan-500"
                    />
                    {errors.instructions && (
                        <FieldError message={errors.instructions} />
                    )}
                </section>

                {/* Actions */}
                <div className="flex justify-end gap-3 pb-6">
                    <Button
                        type="button"
                        variant="secondary"
                        onClick={() =>
                            navigate("/fieldflow/ordenesDeTrabajo")
                        }
                    >
                        Cancelar
                    </Button>

                    <Button
                        type="submit"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Creando..." : "Crear OT"}
                    </Button>
                </div>
            </form>
        </section>
    );
}

function FieldError({ message }: { message?: string }) {
    if (!message) return null;

    return (
        <p className="mt-1 text-xs text-red-400">
            {message}
        </p>
    );
}




export default AddWorkOrderPage;

