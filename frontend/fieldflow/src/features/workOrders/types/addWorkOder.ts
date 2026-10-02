


export type AddWorkOrder = {
    equipmentId: string;
    serviceTypeId: string;
    instructions: string;
    priority: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
    estimatedDurationMinutes: number;
}