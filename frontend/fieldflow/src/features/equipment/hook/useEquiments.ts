import { useQuery } from "@tanstack/react-query";
import { getEquipments } from "@/features/equipment/services/equimentServices";



export function useEquipment() {
    return useQuery({
        queryKey: ["equipment"],
        queryFn: async () => {
            console.log("🔥 Ejecutando getEquipments...");

            const data = await getEquipments();

            console.log("✅ Respuesta:", data);

            return data;
        },
    });
}