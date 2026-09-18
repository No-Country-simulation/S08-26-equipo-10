export type WorkOrderPriority =
    | 'LOW'
    | 'MEDIUM'
    | 'HIGH'
    | 'CRITICAL';

export type WorkOrderStatus =
    | 'PENDING'
    | 'ASSIGNED'
    | 'EN_ROUTE'
    | 'IN_PROGRESS'
    | 'PENDING_CUSTOMER_CONFIRMATION'
    | 'COMPLETED'
    | 'RESCHEDULED';

export type Equipment = {
    id: string;
    identifier: string;
    name: string;
};

export type ServiceType = {
    id: string;
    name: string;
    instructions: string;
};

export type WorkOrder = {
    id: string;
    equipment: Equipment;
    serviceType: ServiceType;
    priority: WorkOrderPriority;
    estimatedDurationMinutes: number;
    status: WorkOrderStatus;
};