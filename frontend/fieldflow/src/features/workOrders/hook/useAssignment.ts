import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
    createWorkOrderAssignment,
    type CreateAssignmentData,
} from "@/features/workOrders/services/WorkOrderService";

export function useCreateWorkOrderAssignment() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
            workOrderId,
            assignmentData,
        }: {
            workOrderId: string;
            assignmentData: CreateAssignmentData;
        }) =>
            createWorkOrderAssignment(
                workOrderId,
                assignmentData
            ),

        onSuccess: (_, { workOrderId }) => {
            queryClient.invalidateQueries({
                queryKey: ["work-orders"],
            });
            queryClient.invalidateQueries({
                queryKey: ["work-order", workOrderId],
            });
            console.log("✅ Asignación creada");
        },
        onError: (error) => {
            console.error("❌ Error creando asignación:", error);
        }
    });
}