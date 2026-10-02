import type { WorkOrderStatus } from "@/features/workOrders/types/workOrder";
import { Badge } from "../common/Badge";
import { workOrderStatusLabels } from "@/utils/workOrderTranslation";



interface StatusBadgeProps {
    status: WorkOrderStatus;
}

const statusStyles: Record<WorkOrderStatus, string> = {
    "IN_PROGRESS":
        "border-purple-500/40 bg-purple-500/10 text-purple-300",

    "EN_ROUTE":
        "border-cyan-500/40 bg-cyan-500/10 text-cyan-300",

    "ASSIGNED":
        "border-blue-500/40 bg-blue-500/10 text-blue-300",

    "PENDING_CUSTOMER_CONFIRMATION":
        "border-orange-500/40 bg-orange-500/10 text-orange-300",

    "PENDING":
        "border-yellow-500/40 bg-yellow-500/10 text-yellow-300",

    "COMPLETED":
        "border-emerald-500/40 bg-emerald-500/10 text-emerald-300",

    "RESCHEDULED":
        "border-pink-500/40 bg-pink-500/10 text-pink-300",
};

export function StatusBadge({ status }: StatusBadgeProps) {
    return (
        <Badge className={`${statusStyles[status]}`} >
            {workOrderStatusLabels[status]}
        </Badge>
    );
}