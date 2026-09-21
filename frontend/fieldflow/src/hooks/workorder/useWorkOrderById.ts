

import { useQuery } from '@tanstack/react-query';
import { getWorkOrderByID } from '@/services/WorkOrderService';

export function useWorkOrderById(id: string) {
    return useQuery({
        queryKey: ['work-order', id],
        queryFn: async () => {
            console.log("🔥 Ejecutando getWorkOrderByID...");

            const data = await getWorkOrderByID(id);

            console.log("✅ Respuesta:", data);

            return data;
        },
    });
}