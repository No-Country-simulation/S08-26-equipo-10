
import { useForm } from "react-hook-form";
import { useTechniques } from "@/features/technicians/hook/useTechniques";
import { useCreateWorkOrderAssignment } from "../hook/useAssignment";
import type { CreateAssignmentData } from "../services/WorkOrderService";
import { useState } from "react";

export interface FormAsignacionProps {
    idWorkOrder: string;
    estimateDuration: number;
    onCancel: () => void;
}

interface AssignmentFormData {
    technicianId: string;
    plannedStartAt: string;

}

export function FormAsignacion({
    idWorkOrder,
    estimateDuration,
    onCancel,
}: FormAsignacionProps) {
    const { data: tecnicos = [] } = useTechniques();
    const [isError, setIsError] = useState<string | null>(null);

    const {
        isPending,
        mutate: createAssignment,
    } = useCreateWorkOrderAssignment();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<AssignmentFormData>({
        defaultValues: {
            technicianId: "",
            plannedStartAt: "",
        },
    });

    const onSubmit = (data: AssignmentFormData) => {
        const startDate = new Date(data.plannedStartAt);

        // estimateDuration está expresado en minutos
        const endDate = new Date(
            startDate.getTime() + estimateDuration * 60 * 1000
        );

        const assignmentData: CreateAssignmentData = {
            technicianId: data.technicianId,
            plannedStartAt: startDate.toISOString(),
            plannedEndAt: endDate.toISOString(),
        };

        console.log("📋 Datos de asignación:", assignmentData);

        createAssignment({
            workOrderId: idWorkOrder,
            assignmentData,
        }, {
            onSuccess: () => {
                onCancel();
            },
            onError: (error) => {
                console.error("❌ Error creando asignación:", error);
                setIsError("Error al crear la asignación");
            }
        });
    };

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-5"
        >
            {/* Técnico */}
            <div>
                <label className="mb-2 block text-sm font-medium text-slate-300">
                    Técnico
                </label>

                <select
                    {...register("technicianId", {
                        required: "Selecciona un técnico",
                    })}
                    className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none focus:border-blue-500"
                >
                    <option value="">
                        Seleccionar técnico
                    </option>

                    {tecnicos.map((tecnico) => (
                        <option
                            key={tecnico.id}
                            value={tecnico.id}
                        >
                            {tecnico.name}
                        </option>
                    ))}
                </select>

                {errors.technicianId && (
                    <p className="mt-1 text-xs text-red-400">
                        {errors.technicianId.message}
                    </p>
                )}

                {tecnicos.length === 0 && (
                    <p className="mt-1 text-xs text-slate-500">
                        No hay técnicos disponibles.
                    </p>
                )}
            </div>

            {/* Fecha de inicio */}
            <div>
                <label className="mb-2 block text-sm font-medium text-slate-300">
                    Fecha y hora de inicio
                </label>

                <input
                    type="datetime-local"
                    {...register("plannedStartAt", {
                        required: "La fecha de inicio es obligatoria",
                    })}
                    className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none focus:border-blue-500"
                />

                {errors.plannedStartAt && (
                    <p className="mt-1 text-xs text-red-400">
                        {errors.plannedStartAt.message}
                    </p>
                )}

                <p className="mt-1 text-xs text-slate-500">
                    La duración estimada de esta orden es de{" "}
                    <span className="font-medium text-slate-400">
                        {estimateDuration} minutos
                    </span>
                    .
                </p>
            </div>

            {/* Información de duración */}
            <div className="rounded-lg border border-slate-700 bg-slate-900/50 p-4">
                <p className="text-xs font-medium uppercase tracking-wide text-slate-500">
                    Duración estimada
                </p>

                <p className="mt-1 text-sm text-slate-300">
                    {estimateDuration} minutos
                </p>

                <p className="mt-1 text-xs text-slate-500">
                    La fecha de finalización se calculará automáticamente.
                </p>
            </div>

            {/* Técnicos disponibles */}
            {tecnicos.length > 0 && (
                <div className="rounded-lg border border-slate-700 bg-slate-900/50 p-4">
                    <p className="text-xs font-medium uppercase tracking-wide text-slate-500">
                        Técnicos disponibles
                    </p>

                    <p className="mt-1 text-sm text-slate-300">
                        {tecnicos.length}{" "}
                        {tecnicos.length === 1
                            ? "técnico disponible"
                            : "técnicos disponibles"}
                    </p>
                </div>
            )}

            {/* Botones */}
            <div className="flex justify-end gap-3 border-t border-slate-700 pt-5">
                <button
                    onClick={onCancel}
                    type="button"
                    disabled={isPending}
                    className="rounded-lg cursor-pointer border border-slate-700 px-4 py-2.5 text-sm font-medium text-slate-300 transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    Cancelar
                </button>

                <button
                    type="submit"
                    disabled={isPending || tecnicos.length === 0}
                    className="rounded-lg cursor-pointer bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    {isPending
                        ? "Asignando..."
                        : "Asignar técnico"}
                </button>
            </div>
            {isError && (
                <p className="mt-2 text-sm text-red-400">
                    {isError}
                </p>
            )}

        </form>
    );
}

