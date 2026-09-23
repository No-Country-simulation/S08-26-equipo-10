// hooks/useWorkOrders.ts

import { useQuery } from '@tanstack/react-query';
import { getWorkOrders } from '@/features/workOrders/services/WorkOrderService';

export function useWorkOrders() {
    return useQuery({
        queryKey: ['work-orders'],
        queryFn: async () => {
            console.log("🔥 Ejecutando getWorkOrders...");

            const data = await getWorkOrders();

            console.log("✅ Respuesta:", data);

            return data;
        },
    });
}