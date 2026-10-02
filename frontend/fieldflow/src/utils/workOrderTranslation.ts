import type {
    WorkOrderPriority,
    WorkOrderStatus,
} from '@/features/workOrders/types/workOrder';

export const workOrderStatusLabels: Record<WorkOrderStatus, string> = {
    PENDING: 'Pendiente',
    ASSIGNED: 'Asignada',
    EN_ROUTE: 'En camino',
    IN_PROGRESS: 'En ejecución',
    PENDING_CUSTOMER_CONFIRMATION: 'Pendiente conformidad',
    COMPLETED: 'Finalizada',
    RESCHEDULED: 'Reprogramada',
};

export const workOrderPriorityLabels: Record<WorkOrderPriority, string> = {
    LOW: 'Baja',
    MEDIUM: 'Media',
    HIGH: 'Alta',
    CRITICAL: 'Crítica',
};