import type { WorkOrderPriority } from "@/types/workOrder";
import { Badge } from "../common/Badge";
import { workOrderPriorityLabels } from "@/utils/workOrderTranslation";




interface PriorityBadgeProps {
    priority: WorkOrderPriority;
}

const priorityStyles: Record<WorkOrderPriority, string> = {
    CRITICAL:
        "border-red-500/40 bg-red-500/10 text-red-400",

    HIGH:
        "border-orange-500/40 bg-orange-500/10 text-orange-400",

    MEDIUM:
        "border-yellow-500/40 bg-yellow-500/10 text-yellow-400",
    LOW:
        "border-green-500/40 bg-green-500/10 text-green-400",
};

export function PriorityBadge({ priority }: PriorityBadgeProps) {
    return (
        <Badge className={priorityStyles[priority]}>
            {workOrderPriorityLabels[priority]}
        </Badge>
    );
}