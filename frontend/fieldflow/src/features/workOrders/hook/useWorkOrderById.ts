

import { useQuery } from '@tanstack/react-query';
import { getWorkOrderByID } from '@/features/workOrders/services/WorkOrderService';

export function useWorkOrderById(id: string) {
    return useQuery({
        queryKey: ['work-order', id],
        queryFn: async () => {

            const data = await getWorkOrderByID(id);


            return data;
        },
    });
}