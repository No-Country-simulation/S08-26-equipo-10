
import type { Equipment } from "@/features/equipment/types/equiment";
import { StatusBadge } from "@/features/equipment/components/StatusBadge";

interface EquipmentTableProps {
    equipment: Equipment[];
    selectedId?: string;
    onSelect: (equipment: Equipment) => void;
}

export function EquipmentTable({
    equipment,
    selectedId,
    onSelect,
}: EquipmentTableProps) {
    return (
        <div className="overflow-x-auto rounded-lg border border-slate-700">
            <table className="w-sm min-w-225">
                <thead className="bg-slate-900">
                    <tr className="border-b border-slate-700">
                        <th className="px-5 py-4 text-left text-sm font-medium text-slate-400">
                            Equipo
                        </th>
                        <th className="px-5 py-4 text-left text-sm font-medium text-slate-400">
                            Cliente
                        </th>
                        <th className="px-5 py-4 text-left text-sm font-medium text-slate-400">
                            Estado
                        </th>
                    </tr>
                </thead>

                <tbody>
                    {equipment.map((item) => {
                        const selected = item.id === selectedId;

                        return (
                            <tr
                                key={item.id}
                                onClick={() => onSelect(item)}
                                className={`
                                    cursor-pointer border-b border-slate-700
                                    transition-colors
                                    ${selected
                                        ? "bg-slate-800"
                                        : "hover:bg-slate-800/60"
                                    }
                                `}
                            >
                                <td className="px-5 py-4">
                                    <div className="font-semibold text-white">
                                        {item.name}
                                    </div>

                                    <div className="text-sm text-slate-400">
                                        {item.identifier}
                                    </div>
                                </td>


                                <td className="max-w-45 truncate px-5 py-4 text-white">
                                    {item.client?.name ?? "-"}
                                </td>





                                <td className="px-5 py-4">
                                    <StatusBadge
                                        status={item.currentStatus}
                                    />
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
}