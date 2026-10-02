import type { Equipment } from "@/features/equipment/types/equiment";

interface EquipmentDetailProps {
    equipment?: Equipment;
}

export function EquipmentDetail({
    equipment,
}: EquipmentDetailProps) {
    if (!equipment) {
        return (
            <div className="flex min-h-60 items-center justify-center rounded-lg border border-slate-700 bg-slate-900/70">
                <p className="text-slate-500">
                    Selecciona un equipo
                </p>
            </div>
        );
    }

    return (
        <aside className="rounded-lg border border-slate-700 bg-slate-900/70 p-6">
            <div className="border-b border-slate-700 pb-5">
                <p className="text-sm text-slate-400">
                    {equipment?.identifier ?? "-"}
                </p>

                <h2 className="mt-1 text-xl font-semibold text-white">
                    {equipment?.name ?? "-"}
                </h2>
            </div>

            <div className="space-y-5 pt-5">
                <DetailItem
                    label="Estado"
                    value={equipment?.currentStatus ?? "-"}
                />

                <DetailItem
                    label="Cliente"
                    value={equipment?.client?.name ?? "-"}
                />

                <DetailItem
                    label="Sede"
                    value={equipment?.site?.name ?? "-"}
                />

                <DetailItem
                    label="Dirección"
                    value={equipment?.site?.address ?? "-"}
                />

                <DetailItem
                    label="Instalación"
                    value={equipment?.installation?.name ?? "-"}
                />
            </div>
        </aside>
    );
}

interface DetailItemProps {
    label: string;
    value: string;
}

function DetailItem({
    label,
    value,
}: DetailItemProps) {
    return (
        <div>
            <p className="text-xs uppercase tracking-wide text-slate-500">
                {label}
            </p>

            <p className="mt-1 text-sm text-slate-200">
                {value}
            </p>
        </div>
    );
}