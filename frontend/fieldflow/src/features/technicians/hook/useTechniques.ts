
// hooks/useWorkOrders.ts

import { useQuery } from '@tanstack/react-query';
import { getTechniques } from '@/features/technicians/services/TechniqueService';

export function useTechniques() {
    return useQuery({
        queryKey: ['techniques'],
        queryFn: async () => {
            console.log("🔥 Ejecutando getTechniques...");

            const data = await getTechniques();

            console.log("✅ Respuesta:", data);

            return data;
        },
    });
}