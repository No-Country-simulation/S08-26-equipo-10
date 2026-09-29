import type { Technique } from "../types/technique";

const API_URL = 'https://fieldflow-api-2lfo.onrender.com/api/v1';

export async function getTechniques(): Promise<Technique[]> {
    const response = await fetch(`${API_URL}/technicians`);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}