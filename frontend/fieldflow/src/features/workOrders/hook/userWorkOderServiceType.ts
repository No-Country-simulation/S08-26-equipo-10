

import { useQuery } from '@tanstack/react-query';
import { getWorkOrderServiceType } from '@/features/workOrders/services/WorkOrderService';

export function useWorkOrdersServiceType() {
    return useQuery({
        queryKey: ['work-orders-service-type'],
        queryFn: async () => {
            console.log("🔥 Ejecutando getWorkOrders service type...");

            const data = await getWorkOrderServiceType();

            console.log("✅ Respuesta:", data);

            return data;
        },
    });
}