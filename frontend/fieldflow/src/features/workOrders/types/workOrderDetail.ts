export type WorkOrderPriority =
    | "LOW"
    | "MEDIUM"
    | "HIGH"
    | "CRITICAL";

export type WorkOrderStatus =
    | "PENDING"
    | "ASSIGNED"
    | "EN_ROUTE"
    | "IN_PROGRESS"
    | "PENDING_CUSTOMER_CONFIRMATION"
    | "COMPLETED"
    | "RESCHEDULED";

export type InterventionStatus =
    | "IN_PROGRESS"
    | "PENDING_CUSTOMER_CONFIRMATION"
    | "COMPLETED";

export interface WorkOrderDetail {
    id: string;
    instructions: string;
    priority: WorkOrderPriority;
    estimatedDurationMinutes: number;
    status: WorkOrderStatus;

    serviceType: {
        id: string;
        name: string;
    };

    equipment: {
        id: string;
        identifier: string;
        name: string;
        currentStatus: string;

        client: {
            id: string;
            name: string;
        };

        site: {
            id: string;
            name: string;
            address: string;
        };

        installation: {
            id: string;
            name: string;
        };
    };

    assignment: {
        id: string;
        technician: {
            id: string;
            name: string;
        };
        plannedStartAt: string;
        plannedEndAt: string;
    };

    checklist: {
        id: string;
        name: string;
        items: ChecklistItem[];
    };

    interventions: Intervention[];
}

export interface ChecklistItem {
    id: string;
    label: string;
}

export interface Intervention {
    id: string;

    technician: {
        id: string;
        name: string;
    };

    startedAt: string;
    endedAt: string;
    status: InterventionStatus;

    result: string;
    observations: string;

    failures: Failure[];
    repairs: Repair[];
    components: Component[];
    checklistAnswers: ChecklistAnswer[];
    technicalNotes: TechnicalNote[];
    evidence: Evidence[];

    conformity: Conformity;
}

export interface Failure {
    id: string;
    description: string;
}

export interface Repair {
    id: string;
    description: string;
}

export interface Component {
    id: string;
    componentName: string;
    action: string;
    description: string;
}

export interface ChecklistAnswer {
    id: string;

    item: {
        id: string;
        label: string;
    };

    value: string;
    observation: string;
}

export interface TechnicalNote {
    id: string;
    content: string;
}

export interface Evidence {
    id: string;
    type: string;
    reference: string;
    description: string;
}

export interface Conformity {
    id: string;
    signature: string;
}