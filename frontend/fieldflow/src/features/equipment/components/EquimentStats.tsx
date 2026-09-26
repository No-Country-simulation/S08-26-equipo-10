import type { Equipment } from "@/features/equipment/types/equiment";

interface EquipmentStatsProps {
    equipment: Equipment[];
}

export function EquipmentStats({
    equipment,
}: EquipmentStatsProps) {
    const operational = equipment.filter(
        (item) => item.currentStatus === "OPERATIONAL"
    ).length;

    const maintenance = equipment.filter(
        (item) => item.currentStatus === "UNDER_MAINTENANCE"
    ).length;

    const toReview = equipment.filter(
        (item) => item.currentStatus === "TO_REVIEW"
    ).length;

    const outOfService = equipment.filter(
        (item) => item.currentStatus === "OUT_OF_SERVICE"
    ).length;

    const stats = [
        {
            label: "Operativo",
            value: operational,
            variant: "success",
        },
        {
            label: "En mantenimiento",
            value: maintenance,
            variant: "info",
        },
        {
            label: "Por revisar",
            value: toReview,
            variant: "warning",
        },
        {
            label: "Fuera de servicio",
            value: outOfService,
            variant: "danger",
        },
    ];

    return (
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4 mb-4">
            {stats.map((stat) => (
                <div
                    key={stat.label}
                    className="rounded-lg border border-slate-700 bg-slate-900/70 p-5"
                >
                    <p className="text-sm uppercase tracking-wide text-slate-400">
                        {stat.label}
                    </p>

                    <p className="mt-2 text-3xl font-semibold text-blue-400">
                        {stat.value}
                    </p>
                </div>
            ))}
        </div>
    );
}