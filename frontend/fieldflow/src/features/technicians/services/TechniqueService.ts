import type { Technique } from "../types/technique";

const API_URL = 'https://fieldflow-api-2lfo.onrender.com/api/v1';

export async function getTechniques(): Promise<Technique[]> {
    const response = await fetch(`${API_URL}/technicians`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}




export async function getTechniciansByDates(
    from: string,
    to: string
): Promise<Technique[]> {
    const params = new URLSearchParams({
        from,
        to,
    });

    const response = await fetch(`${API_URL}/technicians?${params}`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}