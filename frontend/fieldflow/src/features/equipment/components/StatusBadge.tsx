import type { EquipmentStatus } from "../types/equiment";

interface StatusBadgeProps {
    status: EquipmentStatus;
}

export function StatusBadge({ status }: StatusBadgeProps) {
    const config = {
        OPERATIONAL: {
            label: "Operativo",
            className:
                "border-green-500/30 bg-green-500/10 text-green-400",
        },

        UNDER_MAINTENANCE: {
            label: "En mantenimiento",
            className:
                "border-blue-500/30 bg-blue-500/10 text-blue-400",
        },

        REQUIRES_ATTENTION: {
            label: "Por revisar",
            className:
                "border-yellow-500/30 bg-yellow-500/10 text-yellow-400",
        },

        OUT_OF_SERVICE: {
            label: "Fuera de servicio",
            className:
                "border-red-500/30 bg-red-500/10 text-red-400",
        },
    };

    const current = config[status];

    return (
        <span
            className={`
                inline-flex rounded-md border px-3 py-1
                text-xs font-medium
                ${current.className}
            `}
        >
            {current.label}
        </span>
    );
}