import { useState } from "react";

import { EquipmentStats } from "@/features/equipment/components/EquimentStats";
import { EquipmentTable } from "@/features/equipment/components/EquimentTable";
import { EquipmentDetail } from "@/features/equipment/components/EquimentDetail";

import { useEquipment } from "@/features/equipment/hook/useEquiments";

import type { Equipment } from "@/features/equipment/types/equiment";
import { Loading } from "@/components/common/loading";
import { ErrorMessage } from "@/components/common/ErrorMessage";

export default function EquipmentPage() {
    const {
        data: equipment = [],
        isLoading,
        isError,

    } = useEquipment();



    const [selectedEquipment, setSelectedEquipment] =
        useState<Equipment>();




    return (
        <div className="min-h-full p-6 text-white">
            {/* Header */}
            <div className="flex items-start justify-between gap-4 mb-4">
                <div>
                    <h1 className="text-2xl font-bold text-white">
                        Equipos
                    </h1>

                    <p className="mt-1 text-base text-[#78a0d2]">
                        {equipment ? equipment.length : 0} equipos registrados
                    </p>
                </div>

                <button
                    type="button"
                    className="rounded-md bg-blue-600 px-5 py-3 font-medium text-white hover:bg-blue-500"
                >
                    + Nuevo Equipo
                </button>
            </div>

            {/* Stats */}
            <EquipmentStats equipment={equipment!} />



            {/* Content */}
            <div className="grid grid-cols-1 gap-6 xl:grid-cols-[minmax(0,2fr)_minmax(320px,1fr)]">
                {isLoading ? (
                    <Loading />
                ) : isError ? (
                    <ErrorMessage message="Error al cargar los equipos" />
                ) : (
                    <>
                        <EquipmentTable
                            equipment={equipment}
                            selectedId={selectedEquipment?.id}
                            onSelect={setSelectedEquipment}
                        />

                        <EquipmentDetail
                            equipment={selectedEquipment}
                        />
                    </>
                )}
            </div>
        </div>
    );
}


