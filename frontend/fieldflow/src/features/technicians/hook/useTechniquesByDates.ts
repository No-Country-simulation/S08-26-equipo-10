import { useQuery } from '@tanstack/react-query';
import { getTechniciansByDates } from '@/features/technicians/services/TechniqueService';

export function useTechniciansByDates(from: string, to: string) {
    return useQuery({
        queryKey: ['technicians', 'availability', from, to],
        queryFn: async () => {
            console.log("🔥 Ejecutando getTechniciansByDates...");

            const data = await getTechniciansByDates(from, to);

            console.log("✅ Respuesta:", data);

            return data;

        },
        enabled: Boolean(from && to),
    });
}
