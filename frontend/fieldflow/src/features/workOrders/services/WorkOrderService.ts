import type { AddWorkOrder } from "../types/addWorkOder";
import type { WorkOrder } from "../types/workOrder";
import type { WorkOrderDetail } from "../types/workOrderDetail";
import type { WorkOderServiceType } from "../types/workOrderService";


const API_URL = 'https://fieldflow-api-2lfo.onrender.com/api/v1';

export async function getWorkOrders(): Promise<WorkOrder[]> {
    const response = await fetch(`${API_URL}/work-orders`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}

export async function getWorkOrderByID(id: string): Promise<WorkOrderDetail> {
    const response = await fetch(`${API_URL}/work-orders/${id}`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}

export async function getWorkOrderServiceType(): Promise<WorkOderServiceType[]> {
    const response = await fetch(`${API_URL}/service-types`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}


export async function createWorkOrder(
    addWorkOrderData: AddWorkOrder
): Promise<string> {
    const response = await fetch(`${API_URL}/work-orders`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(addWorkOrderData),
    });

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    const data = await response.json();

    return data.id;
}


export interface CreateAssignmentData {
    technicianId: string;
    plannedStartAt: string;
    plannedEndAt: string;
}

export async function createWorkOrderAssignment(
    workOrderId: string,
    assignmentData: CreateAssignmentData
) {
    const response = await fetch(
        `${API_URL}/work-orders/${workOrderId}/assignment`,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(assignmentData),
        }
    );

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}