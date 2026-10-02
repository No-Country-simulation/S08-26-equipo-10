import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createWorkOrder } from '@/features/workOrders/services/WorkOrderService';
import { useNavigate } from 'react-router';


export function useCreateWorkOrder() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    return useMutation({
        mutationFn: createWorkOrder,

        onSuccess: () => {
            console.log("✅ Orden de trabajo creada");

            queryClient.invalidateQueries({
                queryKey: ['work-orders'],
            });
            navigate("/fieldflow/ordenesDeTrabajo");

        },

        onError: (error) => {
            console.error("❌ Error creando orden de trabajo:", error);
        },
    });
}