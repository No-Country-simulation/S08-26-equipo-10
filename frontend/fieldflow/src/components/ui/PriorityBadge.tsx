import { Badge } from "../common/Badge";


type Priority = "Crítica" | "Alta" | "Media";

interface PriorityBadgeProps {
    priority: Priority;
}

const priorityStyles: Record<Priority, string> = {
    Crítica:
        "border-red-500/40 bg-red-500/10 text-red-400",

    Alta:
        "border-orange-500/40 bg-orange-500/10 text-orange-400",

    Media:
        "border-yellow-500/40 bg-yellow-500/10 text-yellow-400",
};

export function PriorityBadge({ priority }: PriorityBadgeProps) {
    return (
        <Badge className={priorityStyles[priority]}>
            {priority}
        </Badge>
    );
}