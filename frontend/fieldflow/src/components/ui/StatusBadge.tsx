import { Badge } from "../common/Badge";


type Status =
    | "En ejecución"
    | "En camino"
    | "Asignada"
    | "Pendiente conformidad"
    | "Pendiente"
    | "Finalizada"
    | "Reprogramada";

interface StatusBadgeProps {
    status: Status;
}

const statusStyles: Record<Status, string> = {
    "En ejecución":
        "border-purple-500/40 bg-purple-500/10 text-purple-300",

    "En camino":
        "border-cyan-500/40 bg-cyan-500/10 text-cyan-300",

    Asignada:
        "border-blue-500/40 bg-blue-500/10 text-blue-300",

    "Pendiente conformidad":
        "border-orange-500/40 bg-orange-500/10 text-orange-300",

    Pendiente:
        "border-yellow-500/40 bg-yellow-500/10 text-yellow-300",

    Finalizada:
        "border-emerald-500/40 bg-emerald-500/10 text-emerald-300",

    Reprogramada:
        "border-pink-500/40 bg-pink-500/10 text-pink-300",
};

export function StatusBadge({ status }: StatusBadgeProps) {
    return (
        <Badge className={statusStyles[status]}>
            {status}
        </Badge>
    );
}