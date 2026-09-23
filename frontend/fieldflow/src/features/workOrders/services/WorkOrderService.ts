import type { WorkOrder } from "../types/workOrder";
import type { WorkOrderDetail } from "../types/workOrderDetail";


const API_URL =
    'https://fieldflow-api-2lfo.onrender.com/api/v1';

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

