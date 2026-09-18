import type { WorkOrder } from '@/types/workOrder';

const API_URL =
    'https://fieldflow-api-2lfo.onrender.com/api/v1';

export async function getWorkOrders(): Promise<WorkOrder[]> {
    const response = await fetch(`${API_URL}/work-orders`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}