import type { Equipment } from "@/features/equipment/types/equiment";

const API_URL = 'https://fieldflow-api-2lfo.onrender.com/api/v1';

export async function getEquipments(): Promise<Equipment[]> {
    const response = await fetch(`${API_URL}/equipments`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}

