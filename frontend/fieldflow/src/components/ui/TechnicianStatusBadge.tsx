import { Badge } from "../common/Badge";


export type TechnicianStatus =
    | "En campo"
    | "Disponible"
    | "Ocupado";

const technicianStatusStyles: Record<TechnicianStatus, string> = {
    "En campo":
        "border-blue-500/40 bg-blue-500/10 text-blue-400",

    Disponible:
        "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",

    Ocupado:
        "border-orange-500/40 bg-orange-500/10 text-orange-400",
};

interface TechnicianStatusBadgeProps {
    status: TechnicianStatus;
}

export function TechnicianStatusBadge({
    status,
}: TechnicianStatusBadgeProps) {
    return (
        <Badge className={technicianStatusStyles[status]}>
            {status}
        </Badge>
    );
}